package com.example.manciu.marketapp

import com.example.manciu.marketapp.data.local.preferences.ThemePreferences
import com.example.manciu.marketapp.di.DaggerAppComponent
import com.example.manciu.marketapp.utils.DarkModeUtils.enableDarkMode
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import timber.log.Timber
import javax.inject.Inject

class MarketApplication : DaggerApplication() {

    @Inject
    lateinit var timber: Timber.Tree

    @Inject
    lateinit var themePreferences: ThemePreferences

    override fun onCreate() {
        super.onCreate()
        Timber.plant(timber)

        if (themePreferences.get())
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