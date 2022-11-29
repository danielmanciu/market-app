package com.example.manciu.marketapp.page.client

import android.os.Bundle
import com.example.manciu.marketapp.R
import com.example.manciu.marketapp.data.local.preferences.ThemePreferences
import com.example.manciu.marketapp.databinding.ActivityClientBinding
import com.example.manciu.marketapp.utils.DarkModeUtils.changeMode
import com.example.manciu.marketapp.utils.DarkModeUtils.isDarkModeEnabled
import com.example.manciu.marketapp.utils.recreateActivity
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class ClientActivity : DaggerAppCompatActivity() {

    private lateinit var binding: ActivityClientBinding

    @Inject
    lateinit var themePreferences: ThemePreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        val isDarkMode = isDarkModeEnabled()

        if (isDarkMode) {
            setTheme(R.style.AppThemeDark)
        }

        super.onCreate(savedInstanceState)
        binding = ActivityClientBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.clientToolbar.darkModeButton.setOnClickListener {
            changeMode()
            recreateActivity(this)
            themePreferences.set(!isDarkMode)
        }
    }
}
