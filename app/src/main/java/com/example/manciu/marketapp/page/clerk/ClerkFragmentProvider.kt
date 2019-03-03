package com.example.manciu.marketapp.page.clerk

import com.example.manciu.marketapp.di.scope.FragmentScope
import com.example.manciu.marketapp.page.clerk.add.AddFragment
import com.example.manciu.marketapp.page.clerk.list.ListFragmentClerk
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ClerkFragmentProvider {

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun provideListFragment(): ListFragmentClerk

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun provideAddFragment(): AddFragment

}