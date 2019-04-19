package com.example.manciu.marketapp.page.client.list.available

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.manciu.marketapp.data.local.persistence.ProductRepository
import com.example.manciu.marketapp.data.remote.RemoteService

class ClientAvailableListViewModelProvider(
        private val service: RemoteService,
        private val repository: ProductRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            ClientAvailableListViewModel(service, repository) as T

}