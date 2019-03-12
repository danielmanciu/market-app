package com.example.manciu.marketapp.page.clerk.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.manciu.marketapp.R
import com.example.manciu.marketapp.base.BaseFragment
import com.example.manciu.marketapp.data.persistence.ProductEntity
import com.example.manciu.marketapp.utils.callback.ItemClickCallback
import kotlinx.android.synthetic.main.fragment_list_clerk.*

class ClerkListFragment : BaseFragment<ClerkListViewModel, ClerkListViewModelProvider>() {

    override fun getViewModelClass() = ClerkListViewModel::class.java

    private lateinit var productsAdapter: ClerkListAdapter

    private val deleteClickCallback = object : ItemClickCallback {
        override fun onClick(product: ProductEntity) {
            if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                deleteProduct(product)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list_clerk, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addButton.setOnClickListener {
            if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                navController.navigate(R.id.action_listFragmentClerk_to_addFragment)
            }
        }

        viewModel.clerkListLiveData.observe(this, Observer {
            hideLoadingIndicator()

            productsAdapter.setProductList(it)
        })

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        productsAdapter = ClerkListAdapter(deleteClickCallback)

        productRecyclerView.adapter = productsAdapter
        productRecyclerView.layoutManager = LinearLayoutManager(context)

        getProductsRemote()
    }

    private fun getProductsRemote() {
        showLoadingIndicator()

        viewModel.getAllProductsRemote()
    }

    private fun deleteProduct(product: ProductEntity) {
        viewModel.deleteProductRemote(product, ::getProductsRemote)
    }

    private fun showLoadingIndicator() {
        addButton.hide()
        productRecyclerView.visibility = View.GONE
        loadingProgressBar.visibility = View.VISIBLE
    }

    private fun hideLoadingIndicator() {
        addButton.show()
        productRecyclerView.visibility = View.VISIBLE
        loadingProgressBar.visibility = View.GONE
    }

}