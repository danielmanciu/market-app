package com.example.manciu.marketapp.utils.callback

import com.example.manciu.marketapp.data.persistence.ProductEntity

interface BuyDialogListener {
    fun buyProduct(productEntity: ProductEntity, quantity: Int)
}