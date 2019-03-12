package com.example.manciu.marketapp.page.client.list.bought

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.manciu.marketapp.R
import com.example.manciu.marketapp.base.BaseFragment
import com.example.manciu.marketapp.utils.showShortToast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_list_client.*
import timber.log.Timber

class ClientBoughtListFragment :
        BaseFragment<ClientBoughtListViewModel, ClientBoughtListViewModelProvider>() {

    override fun getViewModelClass(): Class<ClientBoughtListViewModel> =
            ClientBoughtListViewModel::class.java

    private lateinit var productsAdapter: ClientBoughtListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list_client, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.boughtProductsLiveData.observe(this, Observer {
            hideLoadingIndicator()

            productsAdapter.setProductList(it)
        })

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        showLoadingIndicator()

        productsAdapter = ClientBoughtListAdapter()

        productRecyclerView.adapter = productsAdapter
        productRecyclerView.layoutManager = LinearLayoutManager(context)

        viewModel.getBoughtProductsLocal()
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