package com.example.manciu.marketapp.data.viewmodel

import com.example.manciu.marketapp.data.persistence.ProductRepository
import com.example.manciu.marketapp.data.remote.RemoteService
import com.example.manciu.marketapp.di.scope.ApplicationScope
import dagger.Module
import dagger.Provides

@Module
class ViewModelModule {

    @Provides
    @ApplicationScope
    fun provideViewModelFactory(repository: ProductRepository,
                                service: RemoteService): ProductViewModelFactory =
            ProductViewModelFactory(repository, service)

}