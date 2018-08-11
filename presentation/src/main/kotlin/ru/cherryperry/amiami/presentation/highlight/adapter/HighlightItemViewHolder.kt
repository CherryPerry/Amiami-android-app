package ru.cherryperry.amiami.presentation.highlight.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import ru.cherryperry.amiami.R
import ru.cherryperry.amiami.presentation.highlight.model.HighlightItem
import ru.cherryperry.amiami.presentation.util.ViewDelegate

class HighlightItemViewHolder(
    viewGroup: ViewGroup
) : RecyclerView.ViewHolder(
    LayoutInflater.from(viewGroup.context).inflate(R.layout.highlight_item_rule, viewGroup, false)
) {

    private val textView by ViewDelegate<TextView>(R.id.text)
    private val deleteButton by ViewDelegate<View>(R.id.delete)

    fun bind(item: HighlightItem) {
        textView.text = item.highlightRule.rule
        deleteButton.setOnClickListener { item.removeItemAction(item.highlightRule) }
    }
}
