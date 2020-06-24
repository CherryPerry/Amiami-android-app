package ru.cherryperry.amiami.presentation.main

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.cherryperry.amiami.R

class PaddingItemDecorator(
    context: Context
) : RecyclerView.ItemDecoration() {

    private val insidePadding = context.resources.getDimensionPixelOffset(R.dimen.padding_list_item_inside)

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val layoutParams = view.layoutParams as GridLayoutManager.LayoutParams
        if (layoutParams.spanSize != 1) {
            outRect.set(insidePadding, 0, insidePadding, 0)
        } else {
            outRect.set(insidePadding, insidePadding, insidePadding, insidePadding)
        }
    }
}
