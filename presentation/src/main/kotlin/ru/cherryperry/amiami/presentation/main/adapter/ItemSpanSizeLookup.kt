package ru.cherryperry.amiami.presentation.main.adapter

import android.support.v7.widget.GridLayoutManager

class ItemSpanSizeLookup(
    private val itemAdapter: ItemAdapter,
    private val layoutManager: GridLayoutManager
) : GridLayoutManager.SpanSizeLookup() {

    override fun getSpanSize(position: Int): Int =
        if (itemAdapter.getItemViewType(position) == ItemAdapter.VH_HEADER) {
            layoutManager.spanCount
        } else {
            1
        }
}
