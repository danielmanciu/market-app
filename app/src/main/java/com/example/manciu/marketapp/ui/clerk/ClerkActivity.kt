package com.example.manciu.marketapp.ui.clerk

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.manciu.marketapp.R

class ClerkActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_container)

        savedInstanceState ?: let {
            val fragment = ListFragmentClerk()

            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.fragment_container, fragment, ListFragmentClerk.TAG)
                    .commit()
        }
    }

    fun showAddFragment() {
        val fragment = AddFragment()

        supportFragmentManager
                .beginTransaction()
                .addToBackStack(null)
                .replace(R.id.fragment_container, fragment, AddFragment.TAG)
                .commit()
    }
}