package com.example.manciu.marketapp.remote

import com.example.manciu.marketapp.persistence.ProductEntity
import com.google.gson.annotations.SerializedName

data class ProductRemoteEntity(
        @SerializedName("id")
        var id: Int? = null,

        @SerializedName("name")
        val name: String? = null,

        @SerializedName("description")
        val description: String? = null,

        @SerializedName("quantity")
        val quantity: Int? = null,

        @SerializedName("price")
        val price: Int? = null,

        @SerializedName("status")
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