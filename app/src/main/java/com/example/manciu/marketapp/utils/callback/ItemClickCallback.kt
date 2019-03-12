package com.example.manciu.marketapp.utils.callback

import com.example.manciu.marketapp.data.persistence.ProductEntity

interface ItemClickCallback {
    fun onClick(product: ProductEntity)
}