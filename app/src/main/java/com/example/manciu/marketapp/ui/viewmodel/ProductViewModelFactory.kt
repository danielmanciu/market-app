package com.example.manciu.marketapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.manciu.marketapp.remote.RemoteService
import com.example.manciu.marketapp.repository.Repository

class ProductViewModelFactory(
    private val repository: Repository,
    private val service: RemoteService
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductViewModel::class.java)) {
            return ProductViewModel(repository, service) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}
