package com.example.manciu.marketapp.utils.callback

import com.example.manciu.marketapp.data.persistence.ProductEntity

interface ItemPositionClickCallback {
    fun onClick(product: ProductEntity, position: Int)
}