package com.example.manciu.marketapp.page

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.example.manciu.marketapp.R
import com.example.manciu.marketapp.page.clerk.ClerkActivity
import com.example.manciu.marketapp.page.client.ClientActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        welcomeTextView.animation = AnimationUtils.loadAnimation(this, R.anim.from_top)
        selectUserTextView.animation = AnimationUtils.loadAnimation(this, R.anim.from_bottom)

        AnimationUtils.loadAnimation(this, android.R.anim.fade_in).run {
            startOffset = 500
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
    }
}