package com.example.manciu.marketapp.ui.callback

import com.example.manciu.marketapp.persistence.ProductEntity

interface DialogListener {
    fun buyProduct(productEntity: ProductEntity, quantity: Int)
}