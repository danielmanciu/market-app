package com.example.manciu.marketapp.utils

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.view.isVisible
import com.example.manciu.marketapp.databinding.EmptyLayoutBinding

class EmptyLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleRes) {

    private val binding = EmptyLayoutBinding.inflate(LayoutInflater.from(context), this)

    fun showLoading() {
        isVisible = true
        binding.emptyLayoutProgressBar.isVisible = true
        binding.emptyLayoutTryAgainLayout.isVisible = false
    }

    fun showError(message: String?) {
        binding.emptyLayoutErrorTextView.text = message
        binding.emptyLayoutTryAgainLayout.isVisible = true
        binding.emptyLayoutProgressBar.isVisible = false
    }

    fun hide() {
        isVisible = false
    }

    fun setRetryClickListener(listener: OnClickListener) {
        binding.emptyLayoutTryAgainButton.setOnClickListener(listener)
    }
}
