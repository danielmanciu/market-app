package com.example.manciu.marketapp.page.clerk.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.example.manciu.marketapp.R
import com.example.manciu.marketapp.base.BaseFragment
import com.example.manciu.marketapp.data.local.persistence.ProductEntity
import com.example.manciu.marketapp.databinding.FragmentAddBinding
import com.example.manciu.marketapp.utils.Outcome
import com.example.manciu.marketapp.utils.loadAnimation
import com.example.manciu.marketapp.utils.observeNonNull
import com.example.manciu.marketapp.utils.showShortToast
import kotlinx.android.synthetic.main.toolbar.*

@Suppress("TooManyFunctions")
class AddFragment : BaseFragment<AddViewModel, AddViewModelProvider>() {

    private lateinit var binding: FragmentAddBinding

    override fun getViewModelClass() = AddViewModel::class.java

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentAddBinding.inflate(inflater, container, false).also { binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.confirmButton.setOnClickListener {
            insertProduct()
        }

        binding.addEmptyLayout.setRetryClickListener {
            insertProduct()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        backButton.setOnClickListener { navController.navigateUp() }
        darkModeButton.startAnimation(requireActivity().loadAnimation(R.anim.pop_out))
        darkModeButton.isInvisible = true
    }

    override fun onDestroy() {
        super.onDestroy()

        darkModeButton.startAnimation(requireActivity().loadAnimation(R.anim.pop_in))
        darkModeButton.isVisible = true
    }

    private fun getProductFromInputs() = ProductEntity(
        //this id will be replaced by the binding.server
        id = -1,
        name = "${binding.nameEditText.text}",
        description = "${binding.descriptionEditText.text}",
        quantity = "${binding.quantityEditText.text}".toInt(),
        price = "${binding.priceEditText.text}".toInt(),
        status = "${binding.statusEditText.text}"
    )

    private fun insertProduct() {
        if (areInputsEmpty()) {
            showShortToast(activity, "Inputs cannot be empty.")
            return
        }

        val product: ProductEntity = getProductFromInputs()

        viewModel.insertProductRemote(product)

        viewModel.addProductLiveData.observeNonNull(viewLifecycleOwner) {
            when (it) {
                is Outcome.Progress -> if (it.loading) showLoading() else hideLoading()
                is Outcome.Success -> {
                    showShortToast(activity, "Successfully added ${it.data.name}")
                    navController.navigateUp()
                }
                is Outcome.Failure -> showError("An error occurred. Could not add product.")
            }
        }
    }

    private fun showError(error: String?) = binding.addEmptyLayout.showError(error)

    private fun showLoading() = binding.addEmptyLayout.showLoading()

    private fun hideLoading() = binding.addEmptyLayout.hide()

    private fun areInputsEmpty(): Boolean =
        binding.nameEditText.text.isNullOrBlank()
                || binding.descriptionEditText.text.isNullOrBlank()
                || binding.quantityEditText.text.isNullOrBlank()
                || binding.priceEditText.text.isNullOrBlank()
                || binding.statusEditText.text.isNullOrBlank()
}
