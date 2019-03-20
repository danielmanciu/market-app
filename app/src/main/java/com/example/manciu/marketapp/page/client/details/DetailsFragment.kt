package com.example.manciu.marketapp.page.client.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.manciu.marketapp.R
import com.example.manciu.marketapp.base.BaseFragment
import com.example.manciu.marketapp.data.persistence.ProductEntity
import com.example.manciu.marketapp.utils.ID
import com.example.manciu.marketapp.utils.Outcome
import com.example.manciu.marketapp.utils.observeNonNull
import kotlinx.android.synthetic.main.fragment_details.*

class DetailsFragment : BaseFragment<DetailsViewModel, DetailsViewModelProvider>() {

    override fun getViewModelClass(): Class<DetailsViewModel> = DetailsViewModel::class.java

    private var product: ProductEntity? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let { args ->
            val id: Int = args.getInt(ID)

            viewModel.productLiveData.observeNonNull(this) {
                when (it) {
                    is Outcome.Progress -> if (it.loading) showLoading() else hideLoading()
                    is Outcome.Success -> {
                        product = it.data
                        populateTextViews()
                        hideLoading()
                    }
                    is Outcome.Failure -> showError(it.error.localizedMessage)
                }
            }

            detailsEmptyLayout.setRetryClickListener(View.OnClickListener {
                viewModel.getProductRemote(id)
            })

            viewModel.getProductRemote(id)
        }
    }

    private fun populateTextViews() {
        product?.run {
            nameTextView.text = name
            descriptionTextView.text = description
            quantityTextView.text = "$quantity"
            priceTextView.text = "$price"
            statusTextView.text = status
        }
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