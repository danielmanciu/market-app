package com.example.manciu.marketapp.page

import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.animation.AnimationUtils
import com.example.manciu.marketapp.R
import com.example.manciu.marketapp.data.local.preferences.ThemePreferences
import com.example.manciu.marketapp.page.clerk.ClerkActivity
import com.example.manciu.marketapp.page.client.ClientActivity
import com.example.manciu.marketapp.utils.DarkModeUtils
import com.example.manciu.marketapp.utils.DarkModeUtils.isDarkModeEnabled
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var themePreferences: ThemePreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        val isDarkMode = isDarkModeEnabled()

        if (isDarkMode) {
            setTheme(R.style.AppThemeDark)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        welcomeTextView.animation = AnimationUtils.loadAnimation(this, R.anim.from_top)
        selectUserTextView.animation = AnimationUtils.loadAnimation(this, R.anim.from_bottom)

        AnimationUtils.loadAnimation(this, android.R.anim.fade_in).run {
            startOffset = ANIMATION_START_OFFSET
            logoImage.animation = this
            clerkButton.animation = this
            clientButton.animation = this
        }

        clerkButton.setOnClickListener {
            val intent = Intent(this, ClerkActivity::class.java)
            startActivity(intent)
        }

        clientButton.setOnClickListener {
            val intent = Intent(this, ClientActivity::class.java)
            startActivity(intent)
        }

        darkModeButton.setOnClickListener {
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
