package com.example.manciu.marketapp.data.persistence

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable

class ProductRepository(private val productDatabase: ProductDatabase) {

    fun getBoughtProducts(): Flowable<List<ProductEntity>> =
            productDatabase.dao().getAllProducts()

    fun insertProduct(product: ProductEntity) =
            productDatabase.dao().insertProduct(product)

}