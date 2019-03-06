package com.example.manciu.marketapp.page.client

import com.example.manciu.marketapp.di.scope.FragmentScope
import com.example.manciu.marketapp.page.client.list.ListFragmentClient
import com.example.manciu.marketapp.page.client.list_bought.BoughtListFragmentClient
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ClientFragmentProvider {

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun provideListFragment(): ListFragmentClient

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun provideBoughtListFragment(): BoughtListFragmentClient

}