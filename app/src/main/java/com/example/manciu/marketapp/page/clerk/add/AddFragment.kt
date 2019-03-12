package com.example.manciu.marketapp.page.clerk.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.manciu.marketapp.R
import com.example.manciu.marketapp.base.BaseFragment
import com.example.manciu.marketapp.data.persistence.ProductEntity
import com.example.manciu.marketapp.utils.showShortToast
import kotlinx.android.synthetic.main.fragment_add.*

class AddFragment : BaseFragment<AddViewModel, AddViewModelProvider>() {

    override fun getViewModelClass() = AddViewModel::class.java

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        confirmButton.setOnClickListener {
            insertProduct()
        }
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

        viewModel.addProductLiveData.observe(this, Observer {
            showShortToast(activity, "Successfully added ${it.name}")

            navController.popBackStack()
        })
    }

    private fun areInputsEmpty(): Boolean {
        return nameEditText.text.isNullOrBlank()
                || descriptionEditText.text.isNullOrBlank()
                || quantityEditText.text.isNullOrBlank()
                || priceEditText.text.isNullOrBlank()
                || statusEditText.text.isNullOrBlank()
    }

}