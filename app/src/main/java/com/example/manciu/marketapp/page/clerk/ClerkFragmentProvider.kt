package com.example.manciu.marketapp.page.clerk

import com.example.manciu.marketapp.di.scope.FragmentScope
import com.example.manciu.marketapp.page.clerk.add.AddFragment
import com.example.manciu.marketapp.page.clerk.add.AddModule
import com.example.manciu.marketapp.page.clerk.list.ClerkListFragment
import com.example.manciu.marketapp.page.clerk.list.ClerkListModule
import com.example.manciu.marketapp.page.common.details.DetailsFragment
import com.example.manciu.marketapp.page.common.details.DetailsModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ClerkFragmentProvider {

    @FragmentScope
    @ContributesAndroidInjector(modules = [ClerkListModule::class])
    abstract fun provideListFragment(): ClerkListFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = [AddModule::class])
    abstract fun provideAddFragment(): AddFragment

    @FragmentScope
    @ContributesAndroidInjector(modules = [DetailsModule::class])
    abstract fun provideDetailsFragment(): DetailsFragment
}
