package com.example.manciu.marketapp.ui.client

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.manciu.marketapp.Injection
import com.example.manciu.marketapp.Injection.provideHttpClient
import com.example.manciu.marketapp.R
import com.example.manciu.marketapp.persistence.ProductEntity
import com.example.manciu.marketapp.remote.ProductRemoteEntity
import com.example.manciu.marketapp.ui.callback.DialogListener
import com.example.manciu.marketapp.ui.dialog.BuyDialogFragment
import com.example.manciu.marketapp.ui.callback.ItemClickCallback
import com.example.manciu.marketapp.ui.viewmodel.ProductViewModel
import com.example.manciu.marketapp.utils.Constants.WEB_SOCKET_URL
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_list_client.*
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import java.util.stream.Collectors

class ListFragmentClient : Fragment(), DialogListener {
    companion object {
        val TAG: String? = ListFragmentClient::class.qualifiedName
    }

    private val buyProductClickCallback = object : ItemClickCallback {
        override fun onClick(productEntity: ProductEntity) {
            if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                showBuyDialog(productEntity)
            }
        }
    }

    private lateinit var viewModel: ProductViewModel
    private lateinit var webSocket: WebSocket
    private val disposable = CompositeDisposable()
    private lateinit var productsAdapter: ListAdapterClient
    private lateinit var buyDialogFragment: BuyDialogFragment

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list_client, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerViewAndWebSocket()

        showBoughtProductsButton.setOnClickListener {
            (activity as ClientActivity).showBoughtProductList()
        }
    }

    override fun onStop() {
        super.onStop()

        disposable.clear()
    }

    override fun onDestroy() {
        super.onDestroy()

        webSocket.close(1000, "Successfully closed web socket.")
    }

    private fun setupRecyclerViewAndWebSocket() {
        showLoadingIndicator()

        viewModel = Injection.provideViewModel(activity!!)

        productsAdapter = ListAdapterClient(buyProductClickCallback)

        val httpClient = provideHttpClient()

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

        val disposableList = viewModel.getAvailableProductsRemote()
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
                        {
                            Toast.makeText(activity, "Unable to get available products list.", Toast.LENGTH_SHORT).show()
                            Log.e(BoughtProductListFragmentClient.TAG, "Unable to get available products list.", it)
                        }
                )

        disposable.add(disposableList)
    }

    override fun buyProduct(productEntity: ProductEntity, quantity: Int) {

        if (productEntity.quantity < quantity) {
            Toast.makeText(activity, "Only ${productEntity.quantity} available.", Toast.LENGTH_SHORT).show()
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
                        {
                            Toast.makeText(activity, "Unable to process productEntity puchase.", Toast.LENGTH_SHORT).show()
                            Log.e(TAG, "Unable to process productEntity puchase.", it)  }
                )

        liveData.observe(this, Observer {
            Log.i(TAG, "Sucessfully added ${it.name}.")

            dismissBuyDialog()
            (activity as ClientActivity).showProductDetails(it)
        })

        disposable.add(d)
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

}