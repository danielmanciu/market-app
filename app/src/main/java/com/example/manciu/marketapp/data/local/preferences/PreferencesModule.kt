package com.example.manciu.marketapp.data.local.preferences

import android.content.Context
import android.content.SharedPreferences
import com.example.manciu.marketapp.MarketApplication
import com.example.manciu.marketapp.R
import com.example.manciu.marketapp.di.scope.ApplicationScope
import dagger.Module
import dagger.Provides

@Module
class PreferencesModule {

    @Provides
    @ApplicationScope
    fun provideSharedPreferences(application: MarketApplication) =
            application.getSharedPreferences(application.getString(R.string.app_name), Context.MODE_PRIVATE)

    @Provides
    @ApplicationScope
    fun provideThemePreference(sharedPreferences: SharedPreferences) =
            ThemePreferences(sharedPreferences)
}