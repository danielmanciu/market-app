package com.example.manciu.marketapp.page.client

import android.os.Bundle
import android.view.MotionEvent
import android.view.animation.AnimationUtils
import com.example.manciu.marketapp.R
import com.example.manciu.marketapp.data.local.preferences.ThemePreferences
import com.example.manciu.marketapp.utils.DarkModeUtils.changeMode
import com.example.manciu.marketapp.utils.DarkModeUtils.isDarkModeEnabled
import com.example.manciu.marketapp.utils.recreateActivity
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject

class ClientActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var themePreferences: ThemePreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        val isDarkMode = isDarkModeEnabled()

        if (isDarkMode) {
            setTheme(R.style.AppThemeDark)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client)

        darkModeButton.setOnClickListener {
            changeMode()
            recreateActivity(this)
            themePreferences.set(!isDarkMode)
        }
    }
}