package com.example.manciu.marketapp.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.manciu.marketapp.data.remote.RemoteService
import com.example.manciu.marketapp.data.persistence.ProductRepository

class ProductViewModelFactory(
        private val productRepository: ProductRepository,
        private val service: RemoteService
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductViewModel::class.java)) {
            return ProductViewModel(productRepository, service) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class.")
    }

}
