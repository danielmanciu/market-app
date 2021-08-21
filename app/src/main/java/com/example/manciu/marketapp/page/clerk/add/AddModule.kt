package com.example.manciu.marketapp.page.clerk.add

import com.example.manciu.marketapp.data.remote.RemoteService
import com.example.manciu.marketapp.di.scope.FragmentScope
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class AddModule {

    @Provides
    @FragmentScope
    internal fun provideViewModelProvider(service: RemoteService) =
        AddViewModelProvider(service)
}
