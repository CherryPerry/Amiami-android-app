package ru.cherryperry.amiami.presentation.highlight.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.cherryperry.amiami.domain.model.Model
import ru.cherryperry.amiami.presentation.highlight.model.HighlightHeaderItem
import ru.cherryperry.amiami.presentation.highlight.model.HighlightItem
import ru.cherryperry.amiami.presentation.util.ModelDiffUtilCallback

class HighlightAdapter : ListAdapter<Model, RecyclerView.ViewHolder>(
    ModelDiffUtilCallback()
) {

    companion object {
        private const val VH_HEADER = 0
        private const val VH_ITEM = 1
    }

    override fun getItemId(position: Int) = getItem(position).id

    override fun getItemViewType(position: Int) =
        when (getItem(position)) {
            is HighlightHeaderItem -> VH_HEADER
            is HighlightItem -> VH_ITEM
            else -> throw IllegalArgumentException()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            VH_HEADER -> HighlightHeaderViewHolder(parent)
            VH_ITEM -> HighlightItemViewHolder(parent)
            else -> throw IllegalArgumentException()
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        if (holder is HighlightHeaderViewHolder && item is HighlightHeaderItem) {
            holder.bind(item)
        } else if (holder is HighlightItemViewHolder && item is HighlightItem) {
            holder.bind(item)
        }
    }
}
