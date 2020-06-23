package ru.cherryperry.amiami.presentation.util

import androidx.recyclerview.widget.DiffUtil
import ru.cherryperry.amiami.domain.model.Model

class ModelDiffUtilCallback<TItem : Model> : DiffUtil.ItemCallback<TItem>() {

    override fun areItemsTheSame(oldItem: TItem, newItem: TItem) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: TItem, newItem: TItem) = oldItem == newItem
}
