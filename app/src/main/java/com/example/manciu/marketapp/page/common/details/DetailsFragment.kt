package com.example.manciu.marketapp.page.common.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.transition.TransitionInflater
import com.example.manciu.marketapp.R
import com.example.manciu.marketapp.base.BaseFragment
import com.example.manciu.marketapp.data.local.persistence.ProductEntity
import com.example.manciu.marketapp.databinding.FragmentDetailsBinding
import com.example.manciu.marketapp.utils.EnterSharedElementCallback
import com.example.manciu.marketapp.utils.Outcome
import com.example.manciu.marketapp.utils.PRODUCT
import com.example.manciu.marketapp.utils.observeNonNull

class DetailsFragment : BaseFragment<DetailsViewModel, DetailsViewModelProvider>() {

    private lateinit var binding: FragmentDetailsBinding
    private var product: ProductEntity? = null

    override fun getViewModelClass(): Class<DetailsViewModel> = DetailsViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val transition = TransitionInflater.from(activity)
            .inflateTransition(R.transition.details_transition).apply {
                duration =
                    requireActivity().resources.getInteger(R.integer.details_transition_duration)
                        .toLong()
            }
        sharedElementEnterTransition = transition
        setEnterSharedElementCallback(EnterSharedElementCallback(requireActivity()))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentDetailsBinding.inflate(layoutInflater).also { binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let { args ->
            product = args.getParcelable(PRODUCT)
            val id: Int = product!!.id

            binding.rootCardView.transitionName = "$id-rootCardView"
            binding.nameTextView.transitionName = "$id-name"
            binding.quantityIcon.transitionName = "$id-quantityIcon"
            binding.quantityTextView.transitionName = "$id-quantity"
            binding.priceIcon.transitionName = "$id-priceIcon"
            binding.priceTextView.transitionName = "$id-price"

            viewModel.productLiveData.observeNonNull(viewLifecycleOwner) {
                when (it) {
                    is Outcome.Progress -> if (it.loading) showLoading() else hideLoading()
                    is Outcome.Success -> {
                        product = it.data
                        populateTextViews()
                        hideLoading()
                        binding.detailsSwipeRefreshLayout.isRefreshing = false
                    }
                    is Outcome.Failure -> {
                        showError(it.error.localizedMessage)
                        binding.detailsSwipeRefreshLayout.isRefreshing = false
                    }
                }
            }

            binding.detailsEmptyLayout.setRetryClickListener {
                viewModel.getProductRemote(id)
            }

            binding.detailsSwipeRefreshLayout.setOnRefreshListener {
                viewModel.getProductRemote(id)
            }

            populateTextViews()
        }
    }

//  todo
//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//
//        binding.backButton.setOnClickListener { navController.navigateUp() }
//        with(binding.darkModeButton) {
//            startAnimation(requireActivity().loadAnimation(R.anim.pop_out))
//            isInvisible = true
//        }
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//
//        with(binding.darkModeButton) {
//            startAnimation(requireActivity().loadAnimation(R.anim.pop_in))
//            isVisible = true
//        }
//    }

    private fun populateTextViews() {
        product?.run {
            binding.nameTextView.text = name
            binding.descriptionTextView.text = description
            binding.quantityTextView.text = "$quantity"
            binding.priceTextView.text = "$$price"
            binding.statusTextView.text = status
        }
    }

    private fun showLoading() {
        binding.rootCardView.isVisible = false
        binding.detailsEmptyLayout.showLoading()
    }

    private fun hideLoading() {
        binding.rootCardView.isVisible = true
        binding.detailsEmptyLayout.hide()
    }

    private fun showError(message: String?) {
        binding.detailsEmptyLayout.showError(message)
    }
}
