package com.example.manciu.marketapp.data.remote

object ApiConstants {

//    values for running on AVD
//    const val API_BASE_URL = "http://10.0.2.2:2024"
//    const val WEB_SOCKET_URL = "ws://10.0.2.2.106:2024"

    const val API_BASE_URL = "http://192.168.43.8:2024"
    const val WEB_SOCKET_URL = "ws://192.168.43.8:2024"

    const val ACCEPT_HEADER = "Accept"
    const val APPLICATION_JSON = "application/json"

    object ProductsApi {
        const val BUY_PRODUCT = "/buyProduct"
        const val PRODUCT = "/product"
        const val ALL = "/all"
        const val PRODUCTS = "/products"
        const val ID = "id"
    }

    object ProductRemoteEntity {
        const val SERIALIZED_NAME_ID = "id"
        const val SERIALIZED_NAME_NAME = "name"
        const val SERIALIZED_NAME_DESCRIPTION = "description"
        const val SERIALIZED_NAME_QUANTITY = "quantity"
        const val SERIALIZED_NAME_PRICE = "price"
        const val SERIALIZED_NAME_STATUS = "status"
    }
}