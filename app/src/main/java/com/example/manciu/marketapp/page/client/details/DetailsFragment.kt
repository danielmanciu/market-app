package com.example.manciu.marketapp.page.client.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.transition.TransitionInflater
import com.example.manciu.marketapp.R
import com.example.manciu.marketapp.base.BaseFragment
import com.example.manciu.marketapp.data.persistence.ProductEntity
import com.example.manciu.marketapp.utils.EnterSharedElementCallback
import com.example.manciu.marketapp.utils.Outcome
import com.example.manciu.marketapp.utils.PRODUCT
import com.example.manciu.marketapp.utils.observeNonNull
import kotlinx.android.synthetic.main.fragment_details.*
import kotlinx.android.synthetic.main.toolbar.*

class DetailsFragment : BaseFragment<DetailsViewModel, DetailsViewModelProvider>() {

    override fun getViewModelClass(): Class<DetailsViewModel> = DetailsViewModel::class.java

    private var product: ProductEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        TransitionInflater.from(activity).inflateTransition(R.transition.details_transition).run {
            duration = activity!!.resources.getInteger(R.integer.details_transition_duration).toLong()
            this@DetailsFragment.sharedElementEnterTransition = this
            this@DetailsFragment.setEnterSharedElementCallback(EnterSharedElementCallback(activity!!))
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_details, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let { args ->
            product = args.getParcelable(PRODUCT)
            val id: Int = product!!.id

            rootCardView.transitionName = "$id-rootCardView"
            nameTextView.transitionName = "$id-name"
            quantityIcon.transitionName = "$id-quantityIcon"
            quantityTextView.transitionName = "$id-quantity"
            priceIcon.transitionName = "$id-priceIcon"
            priceTextView.transitionName = "$id-price"

            viewModel.productLiveData.observeNonNull(this) {
                when (it) {
                    is Outcome.Progress -> if (it.loading) showLoading() else hideLoading()
                    is Outcome.Success -> {
                        product = it.data
                        populateTextViews()
                        hideLoading()
                        detailsSwipeRefreshLayout.isRefreshing = false
                    }
                    is Outcome.Failure -> {
                        showError(it.error.localizedMessage)
                        detailsSwipeRefreshLayout.isRefreshing = false
                    }
                }
            }

            detailsEmptyLayout.setRetryClickListener(View.OnClickListener {
                viewModel.getProductRemote(id)
            })

            detailsSwipeRefreshLayout.setOnRefreshListener {
                viewModel.getProductRemote(id)
            }

            populateTextViews()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity!!.backButton.setOnClickListener { navController.navigateUp() }
    }

    private fun populateTextViews() =
            product?.run {
                nameTextView.text = name
                descriptionTextView.text = description
                quantityTextView.text = "$quantity"
                priceTextView.text = "$$price"
                statusTextView.text = status
            }

    private fun showLoading() {
        rootCardView.visibility = View.GONE
        detailsEmptyLayout.showLoading()
    }

    private fun hideLoading() {
        rootCardView.visibility = View.VISIBLE
        detailsEmptyLayout.hide()
    }

    private fun showError(message: String?) {
        detailsEmptyLayout.showError(message)
    }

}