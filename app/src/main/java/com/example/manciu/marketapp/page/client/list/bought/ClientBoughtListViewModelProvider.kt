package com.example.manciu.marketapp.page.client.list.bought

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.manciu.marketapp.data.local.persistence.ProductRepository

class ClientBoughtListViewModelProvider(private val repository: ProductRepository) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            ClientBoughtListViewModel(repository) as T
}
