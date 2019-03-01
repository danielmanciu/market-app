package com.example.manciu.marketapp.ui.client

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.manciu.marketapp.R
import com.example.manciu.marketapp.persistence.ProductEntity
import com.example.manciu.marketapp.utils.Constants.PRODUCT
import kotlinx.android.synthetic.main.fragment_details.*

class DetailsFragment : Fragment() {

    companion object {
        val TAG: String? = DetailsFragment::class.qualifiedName
    }

    private var productEntity: ProductEntity? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        productEntity = arguments!!.getParcelable(PRODUCT)

        populateTextViews()
    }

    private fun populateTextViews() {
        nameTextView.text= "Name:".padEnd(20) + productEntity?.name
        descriptionTextView.text= "Description:".padEnd(20) + productEntity?.description
        quantityTextView.text= "Quantity:".padEnd(20) + productEntity?.quantity
        priceTextView.text= "Price:".padEnd(20) + productEntity?.price
        statusTextView.text= "Status:".padEnd(20) + productEntity?.status
    }

}