package com.example.manciu.marketapp.page.client.list_bought

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.manciu.marketapp.R
import com.example.manciu.marketapp.base.BaseFragment
import com.example.manciu.marketapp.utils.showShortToast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_list_client.*
import timber.log.Timber

class BoughtListFragmentClient : BaseFragment() {

    private lateinit var productsAdapter: BoughtListAdapterClient

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list_client, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        showBoughtProductsButton.hide()
    }

    private fun setupRecyclerView() {
        showLoadingIndicator()

        productsAdapter = BoughtListAdapterClient()

        productRecyclerView.adapter = productsAdapter
        productRecyclerView.layoutManager = LinearLayoutManager(context)

        val d: Disposable = viewModel.getBoughtProductsLocal()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    hideLoadingIndicator()

                    productsAdapter.setProductList(it)
                },
                        { error ->
                            showShortToast(activity, "Unable to get product list.")
                            Timber.e(error, "Unable to get product list.")
                        }
                )

        addDisposable(d)
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