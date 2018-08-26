package ru.cherryperry.amiami.presentation.highlight.adapter

import android.content.res.ColorStateList
import android.support.annotation.ColorInt
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
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
    private val typeView by ViewDelegate<TextView>(R.id.type)
    private val deleteButton by ViewDelegate<View>(R.id.delete)
    private val originalColor: ColorStateList
    @ColorInt
    private val errorColor = TypedValue().let {
        typeView.context.theme.resolveAttribute(R.attr.colorError, it, true)
        it.data
    }

    init {
        originalColor = typeView.textColors
    }

    fun bind(item: HighlightItem) {
        textView.text = item.highlightRule.rule
        if (item.highlightRule.regex) {
            if (item.highlightRule.invalidRegex) {
                typeView.setText(R.string.highlight_type_regex_error)
                typeView.setTextColor(errorColor)
            } else {
                typeView.setText(R.string.highlight_type_regex_ok)
                typeView.setTextColor(originalColor)
            }
        } else {
            typeView.setText(R.string.highlight_type_text)
        }
        deleteButton.setOnClickListener { item.removeItemAction(item.highlightRule) }
    }
}
