package com.example.manciu.marketapp.page.client.list.bought

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.manciu.marketapp.base.BaseFragment
import com.example.manciu.marketapp.databinding.FragmentListClientBinding
import com.example.manciu.marketapp.utils.Outcome
import com.example.manciu.marketapp.utils.observeNonNull
import kotlinx.android.synthetic.main.toolbar.*

class ClientBoughtListFragment :
    BaseFragment<ClientBoughtListViewModel, ClientBoughtListViewModelProvider>() {

    private lateinit var productsAdapter: ClientBoughtListAdapter
    private lateinit var binding: FragmentListClientBinding

    override fun getViewModelClass(): Class<ClientBoughtListViewModel> =
        ClientBoughtListViewModel::class.java

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        FragmentListClientBinding.inflate(inflater, container, false).also { binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.boughtProductsLiveData.observeNonNull(viewLifecycleOwner) {
            when (it) {
                is Outcome.Progress -> if (it.loading) showLoading() else hideLoading()
                is Outcome.Success -> {
                    productsAdapter.setProductList(it.data)
                    hideLoading()
                    binding.clientListSwipeRefreshLayout.isRefreshing = false
                }
                is Outcome.Failure -> {
                    showError(it.error.localizedMessage)
                    binding.clientListSwipeRefreshLayout.isRefreshing = false
                }
            }
        }

        binding.clientListEmptyLayout.setRetryClickListener {
            viewModel.getBoughtProductsLocal()
        }

        binding.clientListSwipeRefreshLayout.setOnRefreshListener {
            viewModel.getBoughtProductsLocal()
        }

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

        binding.productRecyclerView.run {
            adapter = productsAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun showLoading() {
        binding.productRecyclerView.isVisible = false
        binding.clientListEmptyLayout.showLoading()
    }

    private fun hideLoading() {
        binding.productRecyclerView.isVisible = true
        binding.clientListEmptyLayout.hide()
    }

    private fun showError(message: String?) {
        binding.clientListEmptyLayout.showError(message)
    }
}
