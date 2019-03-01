package com.example.manciu.marketapp.remote

import io.reactivex.Flowable
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.*

interface RemoteService {

    @GET("/all")
    fun getAllProducts(): Flowable<Response<List<ProductRemoteEntity>>>

    @GET("/products")
    fun getAvailableProducts(): Flowable<Response<List<ProductRemoteEntity>>>

    @DELETE("/product/{id}")
    fun deleteProduct(@Path("id") id: Int): Observable<Response<Void>>

    @POST("/product")
    fun insertProduct(@Body product: ProductRemoteEntity): Observable<Response<ProductRemoteEntity>>

    @POST("/buyProduct")
    fun buyProduct(@Body product: ProductRemoteEntity): Observable<Response<ProductRemoteEntity>>

}