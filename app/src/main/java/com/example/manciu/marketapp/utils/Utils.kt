package com.example.manciu.marketapp.utils

import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

const val ID = "id"

fun showShortToast(context: FragmentActivity?, message: String) =
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

fun <T> LiveData<T>.observeNonNull(lifecycleOwner: LifecycleOwner, action: (T) -> Unit) =
        observe(lifecycleOwner, Observer { it?.let(action) })