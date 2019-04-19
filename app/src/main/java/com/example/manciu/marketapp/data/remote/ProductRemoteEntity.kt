package com.example.manciu.marketapp.data.remote

import com.example.manciu.marketapp.data.local.persistence.ProductEntity
import com.example.manciu.marketapp.data.remote.ApiConstants.ProductRemoteEntity.SERIALIZED_NAME_DESCRIPTION
import com.example.manciu.marketapp.data.remote.ApiConstants.ProductRemoteEntity.SERIALIZED_NAME_ID
import com.example.manciu.marketapp.data.remote.ApiConstants.ProductRemoteEntity.SERIALIZED_NAME_NAME
import com.example.manciu.marketapp.data.remote.ApiConstants.ProductRemoteEntity.SERIALIZED_NAME_PRICE
import com.example.manciu.marketapp.data.remote.ApiConstants.ProductRemoteEntity.SERIALIZED_NAME_QUANTITY
import com.example.manciu.marketapp.data.remote.ApiConstants.ProductRemoteEntity.SERIALIZED_NAME_STATUS
import com.google.gson.annotations.SerializedName

data class ProductRemoteEntity(
        @SerializedName(SERIALIZED_NAME_ID)
        var id: Int? = null,

        @SerializedName(SERIALIZED_NAME_NAME)
        val name: String? = null,

        @SerializedName(SERIALIZED_NAME_DESCRIPTION)
        val description: String? = null,

        @SerializedName(SERIALIZED_NAME_QUANTITY)
        val quantity: Int? = null,

        @SerializedName(SERIALIZED_NAME_PRICE)
        val price: Int? = null,

        @SerializedName(SERIALIZED_NAME_STATUS)
        val status: String? = null
) {
    fun convertRemoteToLocal() = ProductEntity(
            id ?: 0,
            name ?: "",
            description ?: "",
            quantity ?: 0,
            price ?: 0,
            status ?: ""
    )
}