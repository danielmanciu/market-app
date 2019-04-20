package com.example.manciu.marketapp.utils

import androidx.appcompat.app.AppCompatDelegate

object DarkModeUtils {

    fun enableDarkMode() =
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

    fun isDarkModeEnabled() =
            AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES

    fun changeMode() {
        val mode = if (isDarkModeEnabled())
            AppCompatDelegate.MODE_NIGHT_NO
        else
            AppCompatDelegate.MODE_NIGHT_YES

        AppCompatDelegate.setDefaultNightMode(mode)
    }
}