package com.example.manciu.marketapp.page.clerk.list

import com.example.manciu.marketapp.data.remote.RemoteService
import com.example.manciu.marketapp.di.scope.FragmentScope
import dagger.Module
import dagger.Provides

@Module
class ClerkListModule {

    @Provides
    @FragmentScope
    internal fun provideViewModelProvider(service: RemoteService) =
            ClerkListViewModelProvider(service)

}