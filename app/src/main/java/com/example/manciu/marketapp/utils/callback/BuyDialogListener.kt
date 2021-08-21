package com.example.manciu.marketapp.utils.callback

import com.example.manciu.marketapp.data.local.persistence.ProductEntity

interface BuyDialogListener {

    fun buyProduct(product: ProductEntity, quantity: Int, position: Int)
}
