package com.example.manciu.marketapp.page.client

import android.content.SharedPreferences
import android.os.Bundle
import com.example.manciu.marketapp.R
import com.example.manciu.marketapp.utils.DARK_MODE
import com.example.manciu.marketapp.utils.DarkModeUtils.changeMode
import com.example.manciu.marketapp.utils.DarkModeUtils.isDarkModeEnabled
import com.example.manciu.marketapp.utils.recreateActivity
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject

class ClientActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

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
            sharedPreferences.edit().putBoolean(DARK_MODE, !isDarkMode).apply()
        }
    }

}