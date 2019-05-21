package com.example.manciu.marketapp.utils

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.animation.GridLayoutAnimationController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class GridRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : RecyclerView(context, attrs, defStyle) {

    override fun attachLayoutAnimationParameters(child: View?,
                                                 params: ViewGroup.LayoutParams?,
                                                 index: Int,
                                                 count: Int) {
        val layoutManager: LayoutManager? = layoutManager

        if (adapter != null && layoutManager is GridLayoutManager) {
            // If there are no animation parameters, create new one and attach
            // them to the LayoutParams.
            val animationParams = params?.layoutAnimationParameters
                    ?: GridLayoutAnimationController.AnimationParameters().also {
                        params?.layoutAnimationParameters = it
                    }

            // Next: update parameters
            (animationParams as GridLayoutAnimationController.AnimationParameters).run {

                // Set the number of items in the RecyclerView and the index of this item
                this.count = count
                this.index = index

                // Calculate the number of columns and rows in the grid
                val columns = layoutManager.spanCount
                this.columnsCount = columns
                this.rowsCount = count / columns

                // Calculate the column/row position in the grid
                val invertedIndex = count - 1 - index
                this.column = columns - 1 - (invertedIndex % columns)
                this.row = this.rowsCount - 1 - invertedIndex / columns
            }
        } else {
            super.attachLayoutAnimationParameters(child, params, index, count)
        }
    }
}