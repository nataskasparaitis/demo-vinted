package com.vinted.demovinted.core.recyclerview // package (folder path)

import android.graphics.Rect // a rectangle (cell margins)
import android.view.View // a UI element
import androidx.recyclerview.widget.GridLayoutManager // grid arrangement info
import androidx.recyclerview.widget.RecyclerView // the list/grid widget

class EvenSpacingItemDecorator( // even gutters for a 2-column grid
    private val offset: Int // the base gap size in pixels
) : SpacingItemDecoration(offset, VERTICAL_HORIZONTAL_SPACING) { // reuse the all-sides base

    override fun getItemOffsets(outRect: Rect, // margins to fill in for this cell
                                view: View, // the cell view
                                parent: RecyclerView, // the grid
                                state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state) // base spacing first
        val rowSpanCount = 2 // there are 2 columns

        val spanIndex = (view.layoutParams as GridLayoutManager.LayoutParams).spanIndex // which column (0 or 1)
        val spanSize = (view.layoutParams as GridLayoutManager.LayoutParams).spanSize // how many columns wide

        val half = offset / 2 // half gap, used between columns

        outRect.top = half // half gap on top
        outRect.bottom = half // half gap on bottom
        outRect.left = if (spanIndex == 0) offset else half // full gap on the left edge, else half
        outRect.right = // gap on the right...
            if (spanIndex == rowSpanCount - 1 || spanIndex == rowSpanCount - 2 && spanSize == 2) { // last column or full-width
                offset // full gap on the right edge
            } else {
                half // otherwise half
            }
    }
}