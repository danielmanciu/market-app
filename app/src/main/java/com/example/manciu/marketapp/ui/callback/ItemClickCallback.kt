package com.example.manciu.marketapp.ui.callback

import com.example.manciu.marketapp.persistence.ProductEntity

interface ItemClickCallback {
    fun onClick(productEntity: ProductEntity)
}