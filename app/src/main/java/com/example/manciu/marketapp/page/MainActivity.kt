package com.example.manciu.marketapp.page

import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.animation.AnimationUtils
import com.example.manciu.marketapp.R
import com.example.manciu.marketapp.data.local.preferences.ThemePreferences
import com.example.manciu.marketapp.databinding.ActivityMainBinding
import com.example.manciu.marketapp.page.clerk.ClerkActivity
import com.example.manciu.marketapp.page.client.ClientActivity
import com.example.manciu.marketapp.utils.DarkModeUtils
import com.example.manciu.marketapp.utils.DarkModeUtils.isDarkModeEnabled
import com.example.manciu.marketapp.utils.loadAnimation
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var themePreferences: ThemePreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        val isDarkMode = isDarkModeEnabled()

        if (isDarkMode) {
            setTheme(R.style.AppThemeDark)
        }

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.welcomeTextView.animation = loadAnimation(R.anim.from_top)
        binding.selectUserTextView.animation = loadAnimation(R.anim.from_bottom)

        loadAnimation(android.R.anim.fade_in).run {
            startOffset = ANIMATION_START_OFFSET
            binding.logoImage.animation = this
            binding.clerkButton.animation = this
            binding.clientButton.animation = this
        }

        binding.clerkButton.setOnClickListener {
            val intent = Intent(this, ClerkActivity::class.java)
            startActivity(intent)
        }

        binding.clientButton.setOnClickListener {
            val intent = Intent(this, ClientActivity::class.java)
            startActivity(intent)
        }

        binding.darkModeButton.setOnClickListener {
            DarkModeUtils.changeMode()
            recreate()
            themePreferences.set(!isDarkMode)
        }
    }

    // activity has to be recreated if current theme doesn't correspond to selected theme
    override fun onResume() {
        super.onResume()

        val darkMode = isDarkModeEnabled()
        val themeName = if (darkMode) getString(R.string.dark) else getString(R.string.light)

        val outValue = TypedValue()
        theme.resolveAttribute(R.attr.themeName, outValue, true)

        if (outValue.string != themeName) {
            recreate()
        }
    }

    private companion object {
        const val ANIMATION_START_OFFSET = 500L
    }
}
