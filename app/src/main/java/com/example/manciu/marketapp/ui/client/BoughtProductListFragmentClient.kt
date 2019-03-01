package com.example.manciu.marketapp.ui.client

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.manciu.marketapp.Injection
import com.example.manciu.marketapp.R
import com.example.manciu.marketapp.ui.viewmodel.ProductViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_list_client.*

class BoughtProductListFragmentClient : Fragment() {

    companion object {
        val TAG: String? = BoughtProductListFragmentClient::class.qualifiedName
    }

    private lateinit var viewModel: ProductViewModel
    private val disposable = CompositeDisposable()
    private lateinit var productsAdapter: BoughtProductListAdapterClient

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list_client, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        showBoughtProductsButton.hide()
    }

    override fun onStop() {
        super.onStop()

        disposable.clear()
    }

    private fun setupRecyclerView() {
        showLoadingIndicator()

        viewModel = Injection.provideViewModel(activity!!)

        productsAdapter = BoughtProductListAdapterClient()

        productRecyclerView.adapter = productsAdapter
        productRecyclerView.layoutManager = LinearLayoutManager(context)

        val disposableList = viewModel.getBoughtProductsLocal()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                        hideLoadingIndicator()

                        productsAdapter.setProductList(it)
                },
                        {
                            Toast.makeText(activity, "Unable to get product list.", Toast.LENGTH_SHORT).show()
                            Log.e(TAG, "Unable to get product list.", it)
                        }
                )

        disposable.add(disposableList)
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