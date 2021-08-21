package com.example.manciu.marketapp.page.clerk.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.example.manciu.marketapp.R
import com.example.manciu.marketapp.base.BaseFragment
import com.example.manciu.marketapp.data.local.persistence.ProductEntity
import com.example.manciu.marketapp.utils.Outcome
import com.example.manciu.marketapp.utils.observeNonNull
import com.example.manciu.marketapp.utils.showShortToast
import kotlinx.android.synthetic.main.fragment_add.*
import kotlinx.android.synthetic.main.toolbar.*

@Suppress("TooManyFunctions")
class AddFragment : BaseFragment<AddViewModel, AddViewModelProvider>() {

    override fun getViewModelClass() = AddViewModel::class.java

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_add, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        confirmButton.setOnClickListener {
            insertProduct()
        }

        addEmptyLayout.setRetryClickListener(View.OnClickListener {
            insertProduct()
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        backButton.setOnClickListener { navController.navigateUp() }
        darkModeButton.startAnimation(
            AnimationUtils.loadAnimation(requireActivity(), R.anim.pop_out)
        )
        darkModeButton.visibility = View.INVISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()

        darkModeButton.startAnimation(
            AnimationUtils.loadAnimation(requireActivity(), R.anim.pop_in)
        )
        darkModeButton.visibility = View.VISIBLE
    }

    private fun getProductFromInputs() = ProductEntity(
        //this id will be replaced by the server
        id = -1,
        name = "${nameEditText.text}",
        description = "${descriptionEditText.text}",
        quantity = "${quantityEditText.text}".toInt(),
        price = "${priceEditText.text}".toInt(),
        status = "${statusEditText.text}"
    )

    private fun insertProduct() {
        if (areInputsEmpty()) {
            showShortToast(activity, "Inputs cannot be empty.")
            return
        }

        val product: ProductEntity = getProductFromInputs()

        viewModel.insertProductRemote(product)

        viewModel.addProductLiveData.observeNonNull(this) {
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

    private fun showError(error: String?) = addEmptyLayout.showError(error)

    private fun showLoading() = addEmptyLayout.showLoading()

    private fun hideLoading() = addEmptyLayout.hide()

    private fun areInputsEmpty(): Boolean =
        nameEditText.text.isNullOrBlank()
                || descriptionEditText.text.isNullOrBlank()
                || quantityEditText.text.isNullOrBlank()
                || priceEditText.text.isNullOrBlank()
                || statusEditText.text.isNullOrBlank()
}
