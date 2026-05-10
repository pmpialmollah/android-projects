package com.nsoft.mybakery.adapterdecorators

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

class SellProductRecyclerViewSpacingDecorator : ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        val position = parent.getChildAdapterPosition(view)

        val margin = 20

        outRect.top = margin
        outRect.right = margin

        outRect.bottom = margin

        if (position == 0) {
            outRect.left = margin
        }
    }
}
