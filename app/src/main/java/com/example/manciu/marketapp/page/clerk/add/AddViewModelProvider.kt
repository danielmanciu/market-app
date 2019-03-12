package com.example.manciu.marketapp.page.clerk.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.manciu.marketapp.data.remote.RemoteService

class AddViewModelProvider(private val service: RemoteService) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = AddViewModel(service) as T

}