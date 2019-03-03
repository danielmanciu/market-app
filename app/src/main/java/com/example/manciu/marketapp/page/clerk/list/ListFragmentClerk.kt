package com.example.manciu.marketapp.page.clerk.list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.manciu.marketapp.R
import com.example.manciu.marketapp.base.BaseFragment
import com.example.manciu.marketapp.data.persistence.ProductEntity
import com.example.manciu.marketapp.data.remote.ProductRemoteEntity
import com.example.manciu.marketapp.utils.callback.ItemClickCallback
import com.example.manciu.marketapp.data.viewmodel.ProductViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_list_clerk.*
import timber.log.Timber
import java.util.stream.Collectors

class ListFragmentClerk : BaseFragment() {

    private val deleteClickCallback = object : ItemClickCallback {
        override fun onClick(productEntity: ProductEntity) {
            if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                deleteProduct(productEntity)
            }
        }
    }

    private lateinit var productsAdapter: ListAdapterClerk

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list_clerk, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        addButton.setOnClickListener {
            if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                navController.navigate(R.id.action_listFragmentClerk_to_addFragment)
            }
        }
    }

    private fun setupRecyclerView() {

        productsAdapter = ListAdapterClerk(deleteClickCallback)

        productRecyclerView.adapter = productsAdapter
        productRecyclerView.layoutManager = LinearLayoutManager(context)

        populateAdapter()
    }

    private fun populateAdapter() {
        showLoadingIndicator()

        val d = viewModel.getAllProductsRemote()
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
                        { error -> Timber.e(error,"Unable to get product list.") }
                )

        addDisposable(d)

        hideLoadingIndicator()
    }

    private fun deleteProduct(productEntity: ProductEntity) {

        val d: Disposable = viewModel.deleteProductRemote(productEntity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    populateAdapter()
                }, { error -> Timber.e(error, "Unable to delete productEntity.") })

        addDisposable(d)
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