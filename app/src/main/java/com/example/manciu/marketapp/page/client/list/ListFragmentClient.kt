package com.example.manciu.marketapp.page.client.list

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
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_list_client.*
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import timber.log.Timber
import java.util.stream.Collectors
import javax.inject.Inject

class ListFragmentClient : BaseFragment(), BuyDialogListener {

    private val buyProductClickCallback = object : ItemClickCallback {
        override fun onClick(productEntity: ProductEntity) {
            if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                showBuyDialog(productEntity)
            }
        }
    }

    @Inject
    lateinit var httpClient: OkHttpClient

    private lateinit var webSocket: WebSocket
    private lateinit var productsAdapter: ListAdapterClient
    private lateinit var buyDialogFragment: BuyDialogFragment

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list_client, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerViewAndWebSocket()

        showBoughtProductsButton.setOnClickListener {
            navController.navigate(R.id.action_listFragmentClient_to_boughtListFragmentClient)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        webSocket.close(1000, "Successfully closed web socket.")
    }

    private fun setupRecyclerViewAndWebSocket() {
        showLoadingIndicator()

        productsAdapter = ListAdapterClient(buyProductClickCallback)

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

        val disposable = viewModel.getAvailableProductsRemote()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    response.body()?.let {
                        hideLoadingIndicator()

                        productsAdapter.setProductList(it.stream()
                                .map(ProductRemoteEntity::convertRemoteToLocal)
                                .collect(Collectors.toList()))
                    }

                },
                        { error ->
                            showShortToast(activity, "Unable to get available products list.")
                            Timber.e(error, "Unable to get available products list.")
                        }
                )

        addDisposable(disposable)
    }

    override fun buyProduct(productEntity: ProductEntity, quantity: Int) {

        if (productEntity.quantity < quantity) {
            showShortToast(activity, "Only ${productEntity.quantity} available.")
            return
        }

        val boughtProduct = productEntity.copy(quantity = quantity)

        val liveData: MutableLiveData<ProductEntity> = MutableLiveData()

        val d: Disposable = viewModel.buyProduct(boughtProduct)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    response.body()?.let {
                        viewModel.insertProductLocal(boughtProduct)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe {
                                    liveData.value = boughtProduct
                                }
                    }
                },
                        { error ->
                            showShortToast(activity, "Unable to process productEntity puchase.")
                            Timber.e(error, "Unable to process productEntity puchase.")
                        }
                )

        liveData.observe(this, Observer {
            dismissBuyDialog()
            navigateToDetailsFragment(it)

            Timber.i("Successfully added ${it.name}.")
        })

        addDisposable(d)
    }

    private fun showBuyDialog(productEntity: ProductEntity) {
        fragmentManager?.let {
            buyDialogFragment = BuyDialogFragment.createBuyDialogFragment(it, this, productEntity)
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

        navController.navigate(R.id.action_listFragmentClient_to_detailsFragment, productBundle)
    }

}