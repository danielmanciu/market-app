package com.example.manciu.marketapp.page.client.list.available

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.manciu.marketapp.R
import com.example.manciu.marketapp.base.BaseFragment
import com.example.manciu.marketapp.data.persistence.ProductEntity
import com.example.manciu.marketapp.data.remote.ApiConstants.WEB_SOCKET_URL
import com.example.manciu.marketapp.data.remote.ProductRemoteEntity
import com.example.manciu.marketapp.utils.BuyDialogFragment
import com.example.manciu.marketapp.utils.PRODUCT
import com.example.manciu.marketapp.utils.callback.BuyDialogListener
import com.example.manciu.marketapp.utils.callback.ItemClickCallback
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

    private val buyProductClickCallback = object : ItemClickCallback {
        override fun onClick(product: ProductEntity) {
            if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                showBuyDialog(product)
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

        viewModel.availableListLiveData.observe(this, Observer {
            productsAdapter.setProductList(it)

            hideLoadingIndicator()
        })

        setupRecyclerViewAndWebSocket()
    }

    override fun onDestroy() {
        super.onDestroy()

        webSocket.close(1000, "Successfully closed web socket.")
    }

    private fun setupRecyclerViewAndWebSocket() {
        productsAdapter = ClientAvailableListAdapter(buyProductClickCallback)

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
        productRecyclerView.layoutManager = LinearLayoutManager(context)

        showLoadingIndicator()

        viewModel.getAvailableProductsRemote()
    }

    override fun buyProduct(product: ProductEntity, quantity: Int) {
        if (product.quantity < quantity) {
            showShortToast(activity, "Only ${product.quantity} available.")
            return
        }

        val productToBuy = product.copy(quantity = quantity)

        val buyProductCallback: (ProductEntity) -> Unit = {
            dismissBuyDialog()
            navigateToDetailsFragment(it)

            Timber.i("Successfully added ${it.name}.")
        }

        viewModel.buyProduct(productToBuy, buyProductCallback)
    }

    private fun showBuyDialog(product: ProductEntity) {
        fragmentManager?.let {
            buyDialogFragment = BuyDialogFragment.createBuyDialogFragment(it, this, product)
        }
    }

    private fun dismissBuyDialog() {
        buyDialogFragment.dismiss()
    }

    private fun showLoadingIndicator() {
        productRecyclerView.visibility = View.GONE
        loadingProgressBar.visibility = View.VISIBLE
    }

    private fun hideLoadingIndicator() {
        productRecyclerView.visibility = View.VISIBLE
        loadingProgressBar.visibility = View.GONE
    }

    private fun navigateToDetailsFragment(product: ProductEntity) {
        val productBundle = Bundle()
        productBundle.putParcelable(PRODUCT, product)

        navController.navigate(R.id.action_viewPagerFragment_to_detailsFragment, productBundle)
    }

}