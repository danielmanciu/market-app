package com.example.manciu.marketapp.page.client.list.bought

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.manciu.marketapp.R
import com.example.manciu.marketapp.base.BaseFragment
import com.example.manciu.marketapp.utils.Outcome
import com.example.manciu.marketapp.utils.observeNonNull
import kotlinx.android.synthetic.main.fragment_list_client.*
import kotlinx.android.synthetic.main.toolbar.*

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

        viewModel.boughtProductsLiveData.observeNonNull(this) {
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
            viewModel.getBoughtProductsLocal()
        })

        setupRecyclerView()
        viewModel.getBoughtProductsLocal()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity?.run {
            backButton.setOnClickListener { onBackPressed() }
        }
    }

    private fun setupRecyclerView() {
        productsAdapter = ClientBoughtListAdapter()

        productRecyclerView.adapter = productsAdapter
        productRecyclerView.layoutManager = LinearLayoutManager(context)
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