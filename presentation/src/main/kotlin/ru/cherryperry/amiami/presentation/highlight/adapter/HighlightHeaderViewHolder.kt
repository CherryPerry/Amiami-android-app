package ru.cherryperry.amiami.presentation.highlight.adapter

import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import ru.cherryperry.amiami.R
import ru.cherryperry.amiami.presentation.highlight.model.HighlightHeaderItem
import ru.cherryperry.amiami.presentation.util.ViewDelegate

class HighlightHeaderViewHolder(
    viewGroup: ViewGroup
) : RecyclerView.ViewHolder(
    LayoutInflater.from(viewGroup.context).inflate(R.layout.highlight_item_header, viewGroup, false)
) {

    private val addButton by ViewDelegate<View>(R.id.add)
    private val editView by ViewDelegate<EditText>(R.id.edit)

    fun bind(item: HighlightHeaderItem) {
        addButton.setOnClickListener {
            item.addItemAction(editView.text.toString())
            editView.text = null
        }
        addButton.isEnabled = item.validateInputAction(editView.text)
        editView.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(editable: Editable) {
                addButton.isEnabled = item.validateInputAction(editable)
            }

            override fun beforeTextChanged(sequence: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(sequence: CharSequence, start: Int, before: Int, count: Int) {}
        })
    }
}
