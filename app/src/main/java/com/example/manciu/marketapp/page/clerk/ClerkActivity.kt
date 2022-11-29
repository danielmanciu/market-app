package com.example.manciu.marketapp.page.clerk

import android.os.Bundle
import com.example.manciu.marketapp.R
import com.example.manciu.marketapp.data.local.preferences.ThemePreferences
import com.example.manciu.marketapp.databinding.ActivityClerkBinding
import com.example.manciu.marketapp.utils.DarkModeUtils.changeMode
import com.example.manciu.marketapp.utils.DarkModeUtils.isDarkModeEnabled
import com.example.manciu.marketapp.utils.recreateActivity
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class ClerkActivity : DaggerAppCompatActivity() {

    private lateinit var binding: ActivityClerkBinding

    @Inject
    lateinit var themePreferences: ThemePreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        val isDarkMode = isDarkModeEnabled()

        if (isDarkMode) {
            setTheme(R.style.AppThemeDark)
        }

        super.onCreate(savedInstanceState)
        binding = ActivityClerkBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.clerkToolbar.darkModeButton.setOnClickListener {
            changeMode()
            recreateActivity(this)
            themePreferences.set(!isDarkMode)
        }
    }
}

