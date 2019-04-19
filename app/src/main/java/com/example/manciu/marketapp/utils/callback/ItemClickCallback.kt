package com.example.manciu.marketapp.utils.callback

import android.view.View
import com.example.manciu.marketapp.data.local.persistence.ProductEntity

interface ItemClickCallback {
    fun onClick(product: ProductEntity, productView: View)
}