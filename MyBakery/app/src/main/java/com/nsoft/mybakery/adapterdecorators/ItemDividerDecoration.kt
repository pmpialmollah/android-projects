package com.nsoft.mybakery.adapterdecorators

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class ItemDividerDecoration(
    private val color: Int,
    private val heightPx: Int = 2,
    private val marginStartPx: Int = 0,
    private val marginEndPx: Int = 0
) : RecyclerView.ItemDecoration() {

    private val paint = Paint().apply {
        this.color = color
        isAntiAlias = true
    }

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val childCount = parent.childCount

        for (i in 0 until childCount - 1) { // শেষ item-এর পরে divider দেবো না
            val child = parent.getChildAt(i)

            // item-এর bottom-এ divider draw করবো
            val left = parent.paddingLeft + marginStartPx
            val right = parent.width - parent.paddingRight - marginEndPx
            val top = child.bottom
            val bottom = top + heightPx

            canvas.drawRect(
                left.toFloat(),
                top.toFloat(),
                right.toFloat(),
                bottom.toFloat(),
                paint
            )
        }
    }

    // Divider-এর জন্য item-এর নিচে space রাখতে হবে
    override fun getItemOffsets(
        outRect: Rect, view: View,
        parent: RecyclerView, state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val itemCount = parent.adapter?.itemCount ?: 0

        // শেষ item-এ offset দেবো না
        if (position < itemCount - 1) {
            outRect.bottom = heightPx
        }
    }
}