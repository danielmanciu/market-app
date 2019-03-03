package com.example.manciu.marketapp

import com.example.manciu.marketapp.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import timber.log.Timber
import javax.inject.Inject

class MarketApplication : DaggerApplication() {

    @Inject
    lateinit var timber: Timber.Tree

    override fun onCreate() {
        super.onCreate()
        Timber.plant(timber)
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        val appComponent = DaggerAppComponent.builder()
                .application(this)
                .build()
        appComponent.inject(this)

        return appComponent
    }

}