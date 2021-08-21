package com.example.manciu.marketapp.di

import android.app.Application
import android.content.Context
import com.example.manciu.marketapp.MarketApplication
import com.example.manciu.marketapp.di.scope.ApplicationScope
import dagger.Module
import dagger.Provides
import timber.log.Timber

@Module
class AppModule {

    @Provides
    @ApplicationScope
    internal fun provideApplication(application: Application): MarketApplication = application as MarketApplication

    @Provides
    @ApplicationScope
    internal fun provideContext(application: Application): Context = application

    @Provides
    @ApplicationScope
    internal fun provideTimberTree(): Timber.Tree = Timber.DebugTree()

}
