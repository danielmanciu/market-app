package com.example.manciu.marketapp.page.client.list.available

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewbinding.ViewBinding
import com.example.manciu.marketapp.R
import com.example.manciu.marketapp.base.BaseFragment
import com.example.manciu.marketapp.data.local.persistence.ProductEntity
import com.example.manciu.marketapp.data.remote.ApiConstants.WEB_SOCKET_URL
import com.example.manciu.marketapp.data.remote.ProductRemoteEntity
import com.example.manciu.marketapp.databinding.FragmentListClientBinding
import com.example.manciu.marketapp.databinding.ItemProductClerkBinding
import com.example.manciu.marketapp.page.dialog.BuyDialogFragment
import com.example.manciu.marketapp.utils.Outcome
import com.example.manciu.marketapp.utils.PRODUCT
import com.example.manciu.marketapp.utils.callback.BuyDialogListener
import com.example.manciu.marketapp.utils.callback.ItemClickCallback
import com.example.manciu.marketapp.utils.callback.ItemPositionClickCallback
import com.example.manciu.marketapp.utils.observeNonNull
import com.example.manciu.marketapp.utils.showShortToast
import com.google.gson.Gson
import kotlinx.android.synthetic.main.toolbar.*
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import timber.log.Timber
import javax.inject.Inject

@Suppress("TooManyFunctions")
class ClientAvailableListFragment :
    BaseFragment<ClientAvailableListViewModel, ClientAvailableListViewModelProvider>(),
    BuyDialogListener {

    private lateinit var binding: FragmentListClientBinding

    override fun getViewModelClass() = ClientAvailableListViewModel::class.java

    private val showProductDetailsClickCallback = object : ItemClickCallback {
        override fun onClick(product: ProductEntity, binding: ViewBinding) {
            if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                showDetailsFragment(product, binding)
            }
        }
    }

    private val buyProductClickCallback = object : ItemPositionClickCallback {
        override fun onClick(product: ProductEntity, position: Int) {
            if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                showBuyDialog(product, position)
            }
        }
    }

    @Inject
    lateinit var httpClient: OkHttpClient

    private lateinit var webSocket: WebSocket
    private lateinit var productsAdapter: ClientAvailableListAdapter
    private lateinit var buyDialogFragment: BuyDialogFragment

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        FragmentListClientBinding.inflate(inflater, container, false).also { binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.availableListLiveData.observeNonNull(viewLifecycleOwner) {
            when (it) {
                is Outcome.Progress -> if (it.loading) showLoading() else hideLoading()
                is Outcome.Success -> {
                    productsAdapter.setProductList(it.data)
                    hideLoading()
                    binding.productRecyclerView.startLayoutAnimation()
                    binding.clientListSwipeRefreshLayout.isRefreshing = false
                }
                is Outcome.Failure -> {
                    showError(it.error.localizedMessage)
                    binding.clientListSwipeRefreshLayout.isRefreshing = false
                }
            }
        }

        binding.clientListEmptyLayout.setRetryClickListener {
            viewModel.getAvailableProductsRemote()
        }

        binding.clientListSwipeRefreshLayout.setOnRefreshListener {
            viewModel.getAvailableProductsRemote()
        }

        setupRecyclerViewAndWebSocket()
        viewModel.getAvailableProductsRemote()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity?.run {
            backButton.setOnClickListener { onBackPressed() }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        webSocket.close(WEB_SOCKET_CLOSE_STATUS_CODE, "Successfully closed web socket.")
    }

    private fun setupRecyclerViewAndWebSocket() {
        productsAdapter =
            ClientAvailableListAdapter(buyProductClickCallback, showProductDetailsClickCallback)

        val request: Request = Request.Builder()
            .url(WEB_SOCKET_URL)
            .build()

        webSocket = httpClient.newWebSocket(request, object : WebSocketListener() {
            override fun onMessage(webSocket: WebSocket, text: String) {
                super.onMessage(webSocket, text)

                val product: ProductRemoteEntity =
                    Gson().fromJson<ProductRemoteEntity>(text, ProductRemoteEntity::class.java)

                productsAdapter.addProductAndNotify(product.convertRemoteToLocal())
            }
        })

        binding.productRecyclerView.run {
            adapter = productsAdapter
            layoutManager = GridLayoutManager(context, 2)
            layoutAnimation =
                AnimationUtils.loadLayoutAnimation(context, R.anim.grid_layout_from_bottom)
        }
    }

    override fun buyProduct(product: ProductEntity, quantity: Int, position: Int) {
        if (product.quantity < quantity) {
            showShortToast(activity, "Only ${product.quantity} available.")
            return
        }

        val productToBuy = product.copy(quantity = quantity)

        val buyProductCallback: (ProductEntity) -> Unit = {
            dismissBuyDialog()

            productsAdapter.changeProductAndNotify(it, position)

            Timber.i("Successfully bought ${it.name} (x$quantity).")
        }

        viewModel.buyProduct(productToBuy, buyProductCallback)
    }

    private fun showDetailsFragment(product: ProductEntity, binding: ViewBinding) {
        if (binding !is ItemProductClerkBinding) {
            return
        }

        val productBundle = Bundle()
        productBundle.putParcelable(PRODUCT, product)

        val extras = FragmentNavigatorExtras(
            binding.rootCardView to ViewCompat.getTransitionName(binding.rootCardView)!!,
            binding.productNameTextView to ViewCompat.getTransitionName(binding.productNameTextView)!!,
            binding.quantityIcon to ViewCompat.getTransitionName(binding.quantityIcon)!!,
            binding.productQuantityTextView to ViewCompat.getTransitionName(binding.productQuantityTextView)!!,
            binding.priceIcon to ViewCompat.getTransitionName(binding.priceIcon)!!,
            binding.productPriceTextView to ViewCompat.getTransitionName(binding.productPriceTextView)!!
        )

        navController.navigate(
            R.id.action_viewPagerFragment_to_detailsFragment,
            productBundle,
            null,
            extras
        )
    }

    private fun showBuyDialog(product: ProductEntity, position: Int) {
        buyDialogFragment =
            BuyDialogFragment.createBuyDialogFragment(
                parentFragmentManager,
                this,
                product,
                position
            )
    }

    private fun dismissBuyDialog() = buyDialogFragment.dismiss()

    private fun showLoading() {
        binding.productRecyclerView.isVisible = false
        binding.clientListEmptyLayout.showLoading()
    }

    private fun hideLoading() {
        binding.productRecyclerView.isVisible = true
        binding.clientListEmptyLayout.hide()
    }

    private fun showError(message: String?) = binding.clientListEmptyLayout.showError(message)

    private companion object {
        const val WEB_SOCKET_CLOSE_STATUS_CODE = 1000
    }
}
