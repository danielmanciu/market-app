package com.example.manciu.marketapp.ui.client

import com.example.manciu.marketapp.remote.ProductRemoteEntity
import com.google.gson.Gson
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class MyWebSocketListener(private val adapter: ListAdapterClient
): WebSocketListener() {

    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)

        val product: ProductRemoteEntity =
                Gson().fromJson<ProductRemoteEntity>(text, ProductRemoteEntity::class.java)

        adapter.addProductAndNotify(product.convertRemoteToLocal())
    }

}