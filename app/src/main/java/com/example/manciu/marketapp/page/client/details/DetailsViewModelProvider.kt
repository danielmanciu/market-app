package com.example.manciu.marketapp.page.client.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.manciu.marketapp.data.remote.RemoteService

class DetailsViewModelProvider(private val service: RemoteService): ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        DetailsViewModel(service) as T

}