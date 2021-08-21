package com.example.manciu.marketapp.page.clerk.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.manciu.marketapp.data.remote.RemoteService

class ClerkListViewModelProvider(private val service: RemoteService) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = ClerkListViewModel(service) as T
}
