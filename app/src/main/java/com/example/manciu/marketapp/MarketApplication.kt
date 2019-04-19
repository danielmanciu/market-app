package com.example.manciu.marketapp

import android.content.SharedPreferences
import com.example.manciu.marketapp.di.DaggerAppComponent
import com.example.manciu.marketapp.utils.DARK_MODE
import com.example.manciu.marketapp.utils.DarkModeUtils.enableDarkMode
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import timber.log.Timber
import javax.inject.Inject

class MarketApplication : DaggerApplication() {

    @Inject
    lateinit var timber: Timber.Tree

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate() {
        super.onCreate()
        Timber.plant(timber)

        if (sharedPreferences.getBoolean(DARK_MODE, false))
            enableDarkMode()
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> =
            DaggerAppComponent.builder()
                    .application(this)
                    .build()
                    .also {
                        it.inject(this)
                    }

}