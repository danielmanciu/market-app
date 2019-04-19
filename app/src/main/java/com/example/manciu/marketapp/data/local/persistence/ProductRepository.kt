package com.example.manciu.marketapp.data.local.persistence

import io.reactivex.Flowable

class ProductRepository(private val productDatabase: ProductDatabase) {

    fun getBoughtProducts(): Flowable<List<ProductEntity>> =
            productDatabase.dao().getAllProducts()

    fun insertProduct(product: ProductEntity) =
            productDatabase.dao().insertProduct(product)

}