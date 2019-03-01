package com.example.manciu.marketapp.repository

import com.example.manciu.marketapp.persistence.LocalDatabase
import com.example.manciu.marketapp.persistence.ProductEntity
import io.reactivex.Flowable
import io.reactivex.Observable

class Repository private constructor(private val localDatabase: LocalDatabase) {

    companion object {
        private var INSTANCE: Repository? = null

        fun getInstance(localDatabase: LocalDatabase): Repository =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: Repository(localDatabase).also { INSTANCE = it }
                }
    }

    fun getBoughtProducts(): Flowable<List<ProductEntity>> =
            localDatabase.dao().getAllProducts()

    fun insertProduct(productEntity: ProductEntity) : Observable<Unit> =
            Observable.fromCallable { localDatabase.dao().insertProduct(productEntity) }

}