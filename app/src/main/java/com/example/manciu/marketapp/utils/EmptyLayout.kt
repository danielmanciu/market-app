package com.example.manciu.marketapp.utils

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.example.manciu.marketapp.R
import kotlinx.android.synthetic.main.empty_layout.view.*

class EmptyLayout @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleRes) {

    init {
        LayoutInflater.from(context).inflate(R.layout.empty_layout, this, true)
    }

    fun showLoading() {
        visibility = View.VISIBLE
        emptyLayoutProgressBar.visibility = View.VISIBLE
        emptyLayoutTryAgainLayout.visibility = View.GONE
    }

    fun showError(message: String?) {
        emptyLayoutErrorTextView.text = message
        emptyLayoutTryAgainLayout.visibility = View.VISIBLE
        emptyLayoutProgressBar.visibility = View.GONE
    }

    fun hide() {
        visibility = View.GONE
    }

    fun setRetryClickListener(listener: View.OnClickListener) {
        emptyLayoutTryAgainButton.setOnClickListener(listener)
    }
}