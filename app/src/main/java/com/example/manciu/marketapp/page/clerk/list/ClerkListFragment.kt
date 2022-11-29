package com.example.manciu.marketapp.page.clerk.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewbinding.ViewBinding
import com.example.manciu.marketapp.R
import com.example.manciu.marketapp.base.BaseFragment
import com.example.manciu.marketapp.data.local.persistence.ProductEntity
import com.example.manciu.marketapp.databinding.FragmentListClerkBinding
import com.example.manciu.marketapp.databinding.ItemProductClerkBinding
import com.example.manciu.marketapp.utils.Outcome
import com.example.manciu.marketapp.utils.PRODUCT
import com.example.manciu.marketapp.utils.callback.ItemClickCallback
import com.example.manciu.marketapp.utils.callback.ItemPositionClickCallback
import com.example.manciu.marketapp.utils.observeNonNull
import kotlinx.android.synthetic.main.toolbar.*

class ClerkListFragment : BaseFragment<ClerkListViewModel, ClerkListViewModelProvider>() {

    private lateinit var binding: FragmentListClerkBinding

    override fun getViewModelClass() = ClerkListViewModel::class.java

    private lateinit var productsAdapter: ClerkListAdapter

    private val showProductDetailsClickCallback = object : ItemClickCallback {
        override fun onClick(product: ProductEntity, binding: ViewBinding) {
            if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                showDetailsFragment(product, binding)
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        FragmentListClerkBinding.inflate(inflater, container, false).also { binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.clerkListLiveData.observeNonNull(viewLifecycleOwner) {
            when (it) {
                is Outcome.Progress -> if (it.loading) showLoading() else hideLoading()
                is Outcome.Success -> {
                    productsAdapter.setProductList(it.data)
                    hideLoading()
                    binding.productRecyclerView.startLayoutAnimation()
                    binding.clerkListSwipeRefreshLayout.isRefreshing = false
                }
                is Outcome.Failure -> {
                    showError(it.error.localizedMessage)
                    binding.clerkListSwipeRefreshLayout.isRefreshing = false
                }
            }
        }

        binding.addButton.setOnClickListener {
            if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                navController.navigate(R.id.action_listFragmentClerk_to_addFragment)
            }
        }

        binding.listClerkEmptyLayout.setRetryClickListener {
            viewModel.getAllProductsRemote()
        }

        binding.clerkListSwipeRefreshLayout.setOnRefreshListener {
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

        binding.productRecyclerView.run {
            adapter = productsAdapter
            layoutManager = GridLayoutManager(context, 2)
            layoutAnimation =
                AnimationUtils.loadLayoutAnimation(context, R.anim.grid_layout_from_bottom)
        }
    }

    private fun showDetailsFragment(product: ProductEntity, binding: ViewBinding) {
        if (binding !is ItemProductClerkBinding) {
            return
        }

        val productBundle = Bundle()
        productBundle.putParcelable(PRODUCT, product)

        val extras = FragmentNavigatorExtras(
            binding.rootCardView to ViewCompat.getTransitionName(binding.rootCardView)!!,
            binding.productNameTextView to ViewCompat.getTransitionName(binding.productNameTextView)!!,
            binding.quantityIcon to ViewCompat.getTransitionName(binding.quantityIcon)!!,
            binding.productQuantityTextView to ViewCompat.getTransitionName(binding.productQuantityTextView)!!,
            binding.priceIcon to ViewCompat.getTransitionName(binding.priceIcon)!!,
            binding.productPriceTextView to ViewCompat.getTransitionName(binding.productPriceTextView)!!
        )

        navController.navigate(
            R.id.action_listFragmentClerk_to_detailsFragment,
            productBundle,
            null,
            extras
        )
    }


    private fun deleteProduct(product: ProductEntity, position: Int) {
        val deleteProductCallback: () -> Unit = {
            productsAdapter.deleteProductAndNotify(position)
        }

        viewModel.deleteProductRemote(product, deleteProductCallback)
    }

    private fun showLoading() {
        binding.addButton.hide()
        binding.productRecyclerView.isVisible = false
        binding.listClerkEmptyLayout.showLoading()
    }

    private fun hideLoading() {
        binding.addButton.show()
        binding.productRecyclerView.isVisible = true
        binding.listClerkEmptyLayout.hide()
    }

    private fun showError(message: String?) {
        binding.addButton.hide()
        binding.productRecyclerView.isVisible = false
        binding.listClerkEmptyLayout.showError(message)
    }
}
