package com.example.manciu.marketapp.utils

import android.widget.Toast
import androidx.fragment.app.FragmentActivity

const val PRODUCT = "product"

fun showShortToast(context: FragmentActivity?, message: String) =
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()