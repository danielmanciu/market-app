package com.example.manciu.marketapp.utils

import android.content.Context
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.core.app.SharedElementCallback
import com.example.manciu.marketapp.R
import timber.log.Timber

class EnterSharedElementCallback(context: Context) : SharedElementCallback() {

    private val startTextSize: Float = context.resources.getDimension(R.dimen.item_small_text_size)
    private val endTextSize: Float = context.resources.getDimension(R.dimen.details_large_text_size)
    private val nameStartTextSize: Float = context.resources.getDimension(R.dimen.item_large_text_size)
    private val nameEndTextSize: Float = context.resources.getDimension(R.dimen.details_title_text_size)

    override fun onSharedElementStart(
            sharedElementNames: MutableList<String>?,
            sharedElements: MutableList<View>?,
            sharedElementSnapshots: MutableList<View>?
    ) {
        sharedElements?.run {
            // set nameStartTextTextSize only for nameTextView
            for (i in 0 until this.size) {
                if (sharedElements[i] is TextView)
                    (sharedElements[i] as TextView).setTextSize(TypedValue.COMPLEX_UNIT_PX,
                            sharedElementNames?.get(i)
                                    ?.takeIf { element -> element.contains("name") }
                                    ?.let { nameStartTextSize } ?: startTextSize)
            }
        }
    }

    override fun onSharedElementEnd(
            sharedElementNames: MutableList<String>?,
            sharedElements: MutableList<View>?,
            sharedElementSnapshots: MutableList<View>?
    ) {
        sharedElements?.run {
            for (i in 0 until this.size) {
                if (sharedElements[i] is TextView) {
                    val textView = sharedElements[i] as TextView

                    // get old text view width/height
//                    val oldWidth: Int = textView.measuredWidth
//                    val oldHeight: Int = textView.measuredHeight

                    // set end text size
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                            sharedElementNames?.get(i)
                                    ?.takeIf { element -> element.contains("name") }
                                    ?.let { nameEndTextSize } ?: endTextSize)

                    //FIXME
//                    // re-measure text view since size has changed
//                    val widthSpec: Int = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
//                    val heightSpec: Int = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
//                    textView.measure(widthSpec, heightSpec)

                    // get new text view width/height
//                    val newWidth: Int = textView.measuredWidth
//                    val newHeight: Int = textView.measuredHeight

                    // layout the TextView in the center of its container, accounting for its new width/height.
//                    val widthDiff: Int = newWidth - oldWidth
//                    val heightDiff: Int = newHeight - oldHeight
//                    textView.layout(
//                            textView.left - widthDiff / 2, textView.top - heightDiff / 2,
//                            textView.right + widthDiff / 2, textView.bottom + heightDiff / 2
//                    )
                }
            }
        }
    }
}