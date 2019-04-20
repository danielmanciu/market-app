package com.example.manciu.marketapp.data.local.preferences

import android.content.SharedPreferences
import com.example.manciu.marketapp.data.local.preferences.PreferencesConstants.DARK_MODE

class ThemePreferences(private val preferences: SharedPreferences) {

    fun set(isDarkMode: Boolean) =
            preferences.edit().putBoolean(DARK_MODE, isDarkMode).apply()

    fun get() =
            preferences.getBoolean(DARK_MODE, false)
}
