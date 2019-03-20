package com.example.manciu.marketapp.page.client

import com.example.manciu.marketapp.di.scope.FragmentScope
import com.example.manciu.marketapp.page.client.details.DetailsFragment
import com.example.manciu.marketapp.page.client.details.DetailsModule
import com.example.manciu.marketapp.page.client.list.available.ClientAvailableListFragment
import com.example.manciu.marketapp.page.client.list.available.ClientAvailableListModule
import com.example.manciu.marketapp.page.client.list.bought.ClientBoughtListFragment
import com.example.manciu.marketapp.page.client.list.bought.ClientBoughtListModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ClientFragmentProvider {

    @FragmentScope
    @ContributesAndroidInjector(modules = [ClientAvailableListModule::class])
    abstract fun provideAvailableListFragment(): ClientAvailableListFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = [ClientBoughtListModule::class])
    abstract fun provideBoughtListFragment(): ClientBoughtListFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = [DetailsModule::class])
    abstract fun provideDetailsFragment(): DetailsFragment

}