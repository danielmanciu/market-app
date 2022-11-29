package com.example.manciu.marketapp.utils

import android.app.Activity
import android.content.Context
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.annotation.AnimRes
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

const val PRODUCT = "product"

fun showShortToast(context: FragmentActivity?, message: String) =
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

fun <T> LiveData<T>.observeNonNull(lifecycleOwner: LifecycleOwner, action: (T) -> Unit) =
    observe(lifecycleOwner) { it?.let(action) }

fun recreateActivity(activity: Activity) =
    activity.run {
        startActivity(intent)
        finish()
        overridePendingTransition(0, 0)
    }

fun Context.loadAnimation(@AnimRes animResId: Int) =
    AnimationUtils.loadAnimation(this, animResId)
