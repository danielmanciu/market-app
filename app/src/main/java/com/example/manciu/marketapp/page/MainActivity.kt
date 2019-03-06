package com.example.manciu.marketapp.page

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.manciu.marketapp.R
import com.example.manciu.marketapp.page.clerk.ClerkActivity
import com.example.manciu.marketapp.page.client.ClientActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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