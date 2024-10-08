package com.example.manciu.marketapp.data.remote

object ApiConstants {

//    values for running on AVD
    const val API_BASE_URL = "http://marketapp.themnc.xyz:2024"
    const val WEB_SOCKET_URL = "ws://marketapp.themnc.xy:2024"

//    values for running using the remote server
//    const val API_BASE_URL = "http://market-app.eu-central-1.elasticbeanstalk.com/"
//    const val WEB_SOCKET_URL = "ws://192.168.16.104:2024"

    const val ACCEPT_HEADER = "Accept"
    const val APPLICATION_JSON = "application/json"
    const val TIMEOUT_RETROFIT = 15L

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
