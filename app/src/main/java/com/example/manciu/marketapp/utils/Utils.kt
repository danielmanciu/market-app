package com.example.manciu.marketapp.utils

import android.content.Context
import android.transition.ChangeBounds
import android.transition.Transition
import android.transition.TransitionSet
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

const val PRODUCT = "product"

fun showShortToast(context: FragmentActivity?, message: String) =
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

fun <T> LiveData<T>.observeNonNull(lifecycleOwner: LifecycleOwner, action: (T) -> Unit) =
        observe(lifecycleOwner, Observer { it?.let(action) })

//fun makeSharedElementEnterTransition(context: Context): Transition {
//        val set = TransitionSet()
//        set.ordering = TransitionSet.ORDERING_TOGETHER
//
//        val changeBounds = ChangeBounds()
//        changeBounds.addTarget(R.id.hello_world)
//        changeBounds.addTarget(context.getString(R.string.hello_world))
//        set.addTransition(changeBounds)
//
//        val textSize = TextSizeTransition()
//        textSize.addTarget(R.id.hello_world)
//        textSize.addTarget(context.getString(R.string.hello_world))
//        set.addTransition(textSize)
//
//        return set
//}