package ru.cherryperry.amiami.presentation.main.adapter

import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import ru.cherryperry.amiami.domain.model.Group
import ru.cherryperry.amiami.domain.model.Item
import ru.cherryperry.amiami.domain.model.Model
import ru.cherryperry.amiami.presentation.util.ModelDiffUtilCallback

class ItemAdapter : ListAdapter<Model, RecyclerView.ViewHolder>(
    ModelDiffUtilCallback()
) {

    companion object {
        const val VH_ITEM = 0
        const val VH_HEADER = 1
    }

    var onItemClick: ((item: Item) -> Unit)? = null

    override fun getItemId(position: Int) = getItem(position).id

    override fun getItemViewType(position: Int): Int = when (getItem(position)) {
        is Group -> VH_HEADER
        is Item -> VH_ITEM
        else -> throw IllegalArgumentException()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VH_HEADER -> ItemHeaderHolder(parent)
            VH_ITEM -> ItemHolder(parent) { position -> onItemClick?.invoke(getItem(position) as Item) }
            else -> throw IllegalArgumentException()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        if (holder is ItemHolder && item is Item) {
            holder.bind(item)
        } else if (holder is ItemHeaderHolder && item is Group) {
            holder.bindTime(item.time)
        }
    }
}
