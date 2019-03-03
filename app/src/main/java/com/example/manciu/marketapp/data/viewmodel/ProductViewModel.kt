package com.example.manciu.marketapp.data.viewmodel

import androidx.lifecycle.ViewModel
import com.example.manciu.marketapp.data.persistence.ProductEntity
import com.example.manciu.marketapp.data.remote.ProductRemoteEntity
import com.example.manciu.marketapp.data.remote.RemoteService
import com.example.manciu.marketapp.data.persistence.ProductRepository
import io.reactivex.Flowable
import io.reactivex.Observable
import retrofit2.Response

class ProductViewModel(
        private val productRepository: ProductRepository,
        private val service: RemoteService
) : ViewModel() {

    fun getAllProductsRemote(): Flowable<Response<List<ProductRemoteEntity>>> =
            service.getAllProducts()
                    .filter { it.isSuccessful }

    fun getAvailableProductsRemote(): Flowable<Response<List<ProductRemoteEntity>>> =
            service.getAvailableProducts()
                    .filter { it.isSuccessful }

    fun insertProductRemote(productEntity: ProductEntity): Observable<Response<ProductRemoteEntity>> =
            service.insertProduct(productEntity.convertLocalToRemote())
                    .filter { it.isSuccessful }

    fun deleteProductRemote(productEntity: ProductEntity): Observable<Response<Void>> =
            service.deleteProduct(productEntity.id)
                    .filter { it.isSuccessful }

    fun buyProduct(productEntity: ProductEntity): Observable<Response<ProductRemoteEntity>> =
            service.buyProduct(productEntity.convertLocalToRemote())
                    .filter { it.isSuccessful }

    fun getBoughtProductsLocal(): Flowable<List<ProductEntity>> =
            productRepository.getBoughtProducts()

    fun insertProductLocal(productEntity: ProductEntity): Observable<Unit> =
            productRepository.insertProduct(productEntity)

}