package com.example.manciu.marketapp.page.common.details

import com.example.manciu.marketapp.data.remote.RemoteService
import com.example.manciu.marketapp.di.scope.FragmentScope
import dagger.Module
import dagger.Provides

@Module
class DetailsModule {

    @Provides
    @FragmentScope
    internal fun provideViewModelProvider(service: RemoteService) =
        DetailsViewModelProvider(service)

}