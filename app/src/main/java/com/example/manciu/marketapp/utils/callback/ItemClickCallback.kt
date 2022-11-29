package com.example.manciu.marketapp.utils.callback

import androidx.viewbinding.ViewBinding
import com.example.manciu.marketapp.data.local.persistence.ProductEntity

interface ItemClickCallback {

    fun onClick(product: ProductEntity, binding: ViewBinding)
}
