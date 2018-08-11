package ru.cherryperry.amiami.presentation.highlight

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import ru.cherryperry.amiami.R

class HighlightSeparatorItemDecoration(context: Context) : RecyclerView.ItemDecoration() {

    private val bounds = Rect()
    private val divider = TypedValue().let {
        context.theme.resolveAttribute(R.attr.dividerVertical, it, true)
        ContextCompat.getDrawable(context, it.resourceId)!!
    }

    override fun onDrawOver(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        canvas.save()
        val left: Int
        val right: Int
        if (parent.clipToPadding) {
            left = parent.paddingLeft
            right = parent.width - parent.paddingRight
            canvas.clipRect(left, parent.paddingTop, right, parent.height - parent.paddingBottom)
        } else {
            left = 0
            right = parent.width
        }
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val position = parent.getChildAdapterPosition(child)
            if (position >= parent.adapter.itemCount - 1 || position == RecyclerView.NO_POSITION) {
                continue
            }
            parent.getDecoratedBoundsWithMargins(child, bounds)
            val bottom = bounds.bottom + Math.round(child.translationY)
            val top = bottom - divider.intrinsicHeight
            divider.setBounds(left, top, right, bottom)
            divider.draw(canvas)
        }
        canvas.restore()
    }
}
