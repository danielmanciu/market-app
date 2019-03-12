package com.example.manciu.marketapp.data.remote

import com.example.manciu.marketapp.data.remote.ApiConstants.ProductsApi.ALL
import com.example.manciu.marketapp.data.remote.ApiConstants.ProductsApi.BUY_PRODUCT
import com.example.manciu.marketapp.data.remote.ApiConstants.ProductsApi.ID
import com.example.manciu.marketapp.data.remote.ApiConstants.ProductsApi.PRODUCT
import com.example.manciu.marketapp.data.remote.ApiConstants.ProductsApi.PRODUCTS
import io.reactivex.Flowable
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.*

interface RemoteService {

    @GET(ALL)
    fun getAllProducts(): Flowable<Response<List<ProductRemoteEntity>>>

    @GET(PRODUCTS)
    fun getAvailableProducts(): Flowable<Response<List<ProductRemoteEntity>>>

    @DELETE("$PRODUCT/{$ID}")
    fun deleteProduct(@Path(ID) id: Int): Observable<Response<Unit>>

    @POST(PRODUCT)
    fun insertProduct(@Body product: ProductRemoteEntity): Observable<Response<ProductRemoteEntity>>

    @POST(BUY_PRODUCT)
    fun buyProduct(@Body product: ProductRemoteEntity): Observable<Response<ProductRemoteEntity>>

}