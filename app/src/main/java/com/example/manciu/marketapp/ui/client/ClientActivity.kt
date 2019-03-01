package com.example.manciu.marketapp.ui.client

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.manciu.marketapp.R
import com.example.manciu.marketapp.persistence.ProductEntity
import com.example.manciu.marketapp.ui.clerk.ListFragmentClerk
import com.example.manciu.marketapp.utils.Constants.PRODUCT

class ClientActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_container)

        savedInstanceState ?: let {
            val fragment = ListFragmentClient()

            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.fragment_container, fragment, ListFragmentClerk.TAG)
                    .commit()
        }
    }

    fun showProductDetails(productEntity: ProductEntity) {
        val fragment = DetailsFragment()

        val bundle = Bundle()
        bundle.putParcelable(PRODUCT, productEntity)
        fragment.arguments = bundle

        supportFragmentManager
                .beginTransaction()
                .addToBackStack(null)
                .replace(R.id.fragment_container, fragment, DetailsFragment.TAG)
                .commit()
    }

    fun showBoughtProductList() {
        val fragment = BoughtProductListFragmentClient()

        supportFragmentManager
                .beginTransaction()
                .addToBackStack(null)
                .replace(R.id.fragment_container, fragment, DetailsFragment.TAG)
                .commit()
    }

}