package com.example.manciu.marketapp.page.client.list.bought

import com.example.manciu.marketapp.data.local.persistence.ProductRepository
import com.example.manciu.marketapp.di.scope.FragmentScope
import dagger.Module
import dagger.Provides

@Module
class ClientBoughtListModule {

    @Provides
    @FragmentScope
    internal fun provideViewModelProvider(repository: ProductRepository) =
            ClientBoughtListViewModelProvider(repository)

}