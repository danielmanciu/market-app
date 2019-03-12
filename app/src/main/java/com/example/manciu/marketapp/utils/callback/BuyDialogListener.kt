package com.example.manciu.marketapp.utils.callback

import com.example.manciu.marketapp.data.persistence.ProductEntity

interface BuyDialogListener {
    fun buyProduct(product: ProductEntity, quantity: Int)
}