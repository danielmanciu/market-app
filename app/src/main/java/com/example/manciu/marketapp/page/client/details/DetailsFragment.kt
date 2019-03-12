package com.example.manciu.marketapp.page.client.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.manciu.marketapp.R
import com.example.manciu.marketapp.data.persistence.ProductEntity
import com.example.manciu.marketapp.utils.PRODUCT
import kotlinx.android.synthetic.main.fragment_details.*

class DetailsFragment : Fragment() {

    private var product: ProductEntity? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        product = arguments!!.getParcelable(PRODUCT)

        populateTextViews()
    }

    private fun populateTextViews() {
        product?.run {
            nameTextView.text = name
            descriptionTextView.text =  description
            quantityTextView.text =  "$quantity"
            priceTextView.text = "$price"
            statusTextView.text = status
        }
    }

}