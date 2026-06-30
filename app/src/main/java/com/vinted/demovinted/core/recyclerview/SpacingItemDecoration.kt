package com.vinted.demovinted.core.recyclerview

import android.graphics.Rect // a rectangle (here: margins around a cell)
import android.view.View // a UI element
import androidx.annotation.IntDef // restrict an Int to set of constants
import androidx.recyclerview.widget.RecyclerView // the list/grid widget

open class SpacingItemDecoration( // open = subclassable; adds spacing around cells
    private val offset: Int, // the gap size in pixels
    @Orientation // must be one of the orientation constants
    private val orientation: Int // which sides to space
) : RecyclerView.ItemDecoration() { // base type for list decorations

    @IntDef(VERTICAL_SPACING, HORIZONTAL_SPACING, VERTICAL_HORIZONTAL_SPACING) // allowed values
    annotation class Orientation // the tag type for orientation

    override fun getItemOffsets( // called per cell to set its margins
            outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State // outRect = margins to fill in
    ) {
        super.getItemOffsets(outRect, view, parent, state) // default behavior first

        when (orientation) { // choose margins based on orientation
            HORIZONTAL_SPACING -> outRect.set(offset, 0, offset, 0) // left/right only
            VERTICAL_SPACING -> outRect.set(0, offset, 0, offset) // top/bottom only
            VERTICAL_HORIZONTAL_SPACING -> outRect.set(offset, offset, offset, offset) // all sides
        }
    }

    companion object {
        const val VERTICAL_SPACING = 1 // space top/bottom
        const val HORIZONTAL_SPACING = 2 // space left/right
        const val VERTICAL_HORIZONTAL_SPACING = 3 // space all sides
    }
}