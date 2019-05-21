package com.example.manciu.marketapp.page.clerk.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.view.ViewCompat
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.GridLayoutManager
import com.example.manciu.marketapp.R
import com.example.manciu.marketapp.base.BaseFragment
import com.example.manciu.marketapp.data.local.persistence.ProductEntity
import com.example.manciu.marketapp.utils.Outcome
import com.example.manciu.marketapp.utils.PRODUCT
import com.example.manciu.marketapp.utils.callback.ItemClickCallback
import com.example.manciu.marketapp.utils.callback.ItemPositionClickCallback
import com.example.manciu.marketapp.utils.observeNonNull
import kotlinx.android.synthetic.main.fragment_list_clerk.*
import kotlinx.android.synthetic.main.item_product_clerk.view.*
import kotlinx.android.synthetic.main.toolbar.*

class ClerkListFragment : BaseFragment<ClerkListViewModel, ClerkListViewModelProvider>() {

    override fun getViewModelClass() = ClerkListViewModel::class.java

    private lateinit var productsAdapter: ClerkListAdapter

    private val showProductDetailsClickCallback = object : ItemClickCallback {
        override fun onClick(product: ProductEntity, productView: View) {
            if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                showDetailsFragment(product, productView)
            }
        }
    }

    private val deleteClickCallback = object : ItemPositionClickCallback {
        override fun onClick(product: ProductEntity, position: Int) {
            if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                deleteProduct(product, position)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_list_clerk, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.clerkListLiveData.observeNonNull(this) {
            when (it) {
                is Outcome.Progress -> if (it.loading) showLoading() else hideLoading()
                is Outcome.Success -> {
                    productsAdapter.setProductList(it.data)
                    hideLoading()
                    productRecyclerView.startLayoutAnimation()
                    clerkListSwipeRefreshLayout.isRefreshing = false
                }
                is Outcome.Failure -> {
                    showError(it.error.localizedMessage)
                    clerkListSwipeRefreshLayout.isRefreshing = false
                }
            }
        }

        addButton.setOnClickListener {
            if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                navController.navigate(R.id.action_listFragmentClerk_to_addFragment)
            }
        }

        listClerkEmptyLayout.setRetryClickListener(View.OnClickListener {
            viewModel.getAllProductsRemote()
        })

        clerkListSwipeRefreshLayout.setOnRefreshListener {
            viewModel.getAllProductsRemote()
        }

        setupRecyclerView()

        viewModel.getAllProductsRemote()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity?.run {
            backButton.setOnClickListener { onBackPressed() }
        }
    }

    private fun setupRecyclerView() {
        productsAdapter = ClerkListAdapter(deleteClickCallback, showProductDetailsClickCallback)

        productRecyclerView.run {
            adapter = productsAdapter
            layoutManager = GridLayoutManager(context, 2)
            layoutAnimation = AnimationUtils.loadLayoutAnimation(context, R.anim.grid_layout_from_bottom)
        }
    }

    private fun showDetailsFragment(product: ProductEntity, productView: View) {
        val productBundle = Bundle()
        productBundle.putParcelable(PRODUCT, product)

        val extras = FragmentNavigatorExtras(
            productView.rootCardView to ViewCompat.getTransitionName(productView.rootCardView)!!,
            productView.productNameTextView to ViewCompat.getTransitionName(productView.productNameTextView)!!,
            productView.quantityIcon to ViewCompat.getTransitionName(productView.quantityIcon)!!,
            productView.productQuantityTextView to ViewCompat.getTransitionName(productView.productQuantityTextView)!!,
            productView.priceIcon to ViewCompat.getTransitionName(productView.priceIcon)!!,
            productView.productPriceTextView to ViewCompat.getTransitionName(productView.productPriceTextView)!!
        )

        navController.navigate(R.id.action_listFragmentClerk_to_detailsFragment, productBundle, null, extras)
    }


    private fun deleteProduct(product: ProductEntity, position: Int) {
        val deleteProductCallback: () -> Unit = {
            productsAdapter.deleteProductAndNotify(position)
        }

        viewModel.deleteProductRemote(product, deleteProductCallback)
    }

    private fun showLoading() {
        addButton.hide()
        productRecyclerView.visibility = View.GONE
        listClerkEmptyLayout.showLoading()
    }

    private fun hideLoading() {
        addButton.show()
        productRecyclerView.visibility = View.VISIBLE
        listClerkEmptyLayout.hide()
    }

    private fun showError(message: String?) {
        addButton.hide()
        productRecyclerView.visibility = View.GONE
        listClerkEmptyLayout.showError(message)
    }

}