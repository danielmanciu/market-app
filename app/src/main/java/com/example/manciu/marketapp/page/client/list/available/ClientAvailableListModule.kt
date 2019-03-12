package com.example.manciu.marketapp.page.client.list.available

import com.example.manciu.marketapp.data.persistence.ProductRepository
import com.example.manciu.marketapp.data.remote.RemoteService
import com.example.manciu.marketapp.di.scope.FragmentScope
import dagger.Module
import dagger.Provides

@Module
class ClientAvailableListModule {

    @Provides
    @FragmentScope
    internal fun provideViewModelProvider(service: RemoteService, repository: ProductRepository) =
            ClientAvailableListViewModelProvider(service, repository)

}