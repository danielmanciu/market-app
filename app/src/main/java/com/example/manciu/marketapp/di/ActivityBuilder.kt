package com.example.manciu.marketapp.di

import com.example.manciu.marketapp.di.scope.ActivityScope
import com.example.manciu.marketapp.page.MainActivity
import com.example.manciu.marketapp.page.clerk.ClerkActivity
import com.example.manciu.marketapp.page.clerk.ClerkFragmentProvider
import com.example.manciu.marketapp.page.client.ClientActivity
import com.example.manciu.marketapp.page.client.ClientFragmentProvider
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {

    @ActivityScope
    @ContributesAndroidInjector(modules = [ClerkFragmentProvider::class])
    abstract fun provideClerkActivity(): ClerkActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [ClientFragmentProvider::class])
    abstract fun provideClientActivity(): ClientActivity

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun provideMainActivity(): MainActivity

}