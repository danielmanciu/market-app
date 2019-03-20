package com.example.manciu.marketapp.page.client.list.available

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.manciu.marketapp.R
import com.example.manciu.marketapp.base.BaseFragment
import com.example.manciu.marketapp.data.persistence.ProductEntity
import com.example.manciu.marketapp.data.remote.ApiConstants.WEB_SOCKET_URL
import com.example.manciu.marketapp.data.remote.ProductRemoteEntity
import com.example.manciu.marketapp.page.dialog.BuyDialogFragment
import com.example.manciu.marketapp.utils.ID
import com.example.manciu.marketapp.utils.Outcome
import com.example.manciu.marketapp.utils.callback.BuyDialogListener
import com.example.manciu.marketapp.utils.callback.ItemClickCallback
import com.example.manciu.marketapp.utils.callback.ItemPositionClickCallback
import com.example.manciu.marketapp.utils.observeNonNull
import com.example.manciu.marketapp.utils.showShortToast
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_list_client.*
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import timber.log.Timber
import javax.inject.Inject

class ClientAvailableListFragment : BaseFragment<ClientAvailableListViewModel, ClientAvailableListViewModelProvider>(),
        BuyDialogListener {

    override fun getViewModelClass() = ClientAvailableListViewModel::class.java

    private val showProductDetailsClickCallback = object : ItemClickCallback {
        override fun onClick(product: ProductEntity) {
            if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                showDetailsFragment(product)
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list_client, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.availableListLiveData.observeNonNull(this) {
            when (it) {
                is Outcome.Progress -> if (it.loading) showLoading() else hideLoading()
                is Outcome.Success -> {
                    productsAdapter.setProductList(it.data)
                    hideLoading()
                }
                is Outcome.Failure -> showError(it.error.localizedMessage)
            }
        }

        clientListEmptyLayout.setRetryClickListener(View.OnClickListener {
            viewModel.getAvailableProductsRemote()
        })

        setupRecyclerViewAndWebSocket()
        viewModel.getAvailableProductsRemote()
    }

    override fun onDestroy() {
        super.onDestroy()

        webSocket.close(1000, "Successfully closed web socket.")
    }

    private fun setupRecyclerViewAndWebSocket() {
        productsAdapter = ClientAvailableListAdapter(buyProductClickCallback, showProductDetailsClickCallback)

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

        productRecyclerView.adapter = productsAdapter
        productRecyclerView.layoutManager = GridLayoutManager(context, 2)

        val animation = AnimationUtils.loadLayoutAnimation(context, R.anim.grid_layout_animation_from_bottom)
        productRecyclerView.layoutAnimation = animation
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

    private fun showDetailsFragment(product: ProductEntity) {
        val productBundle = Bundle()
        productBundle.putInt(ID, product.id)

        navController.navigate(R.id.action_viewPagerFragment_to_detailsFragment, productBundle)
    }

    private fun showBuyDialog(product: ProductEntity, position: Int) {
        fragmentManager?.let {
            buyDialogFragment = BuyDialogFragment.createBuyDialogFragment(it, this, product, position)
        }
    }

    private fun dismissBuyDialog() {
        buyDialogFragment.dismiss()
    }

    private fun showLoading() {
        productRecyclerView.visibility = View.GONE
        clientListEmptyLayout.showLoading()
    }

    private fun hideLoading() {
        productRecyclerView.visibility = View.VISIBLE
        clientListEmptyLayout.hide()
    }

    private fun showError(message: String?) {
        clientListEmptyLayout.showError(message)
    }

}