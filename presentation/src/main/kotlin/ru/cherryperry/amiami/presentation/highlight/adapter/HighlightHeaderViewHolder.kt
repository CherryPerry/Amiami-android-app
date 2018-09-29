package ru.cherryperry.amiami.presentation.highlight.adapter

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.CheckBox
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
    private val regexBox by ViewDelegate<CheckBox>(R.id.regex)
    private val editView by ViewDelegate<EditText>(R.id.edit)
    private var bindFor: Int = 0

    init {
        editView.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (addButton.isEnabled) {
                    addButton.performClick()
                }
                true
            } else {
                false
            }
        }
    }

    fun bind(item: HighlightHeaderItem) {
        addButton.setOnClickListener {
            item.addItemAction(editView.text.toString(), regexBox.isChecked)
            editView.text = null
            regexBox.isChecked = false
            hideKeyboard()
        }
        addButton.isEnabled = item.validateInputAction(editView.text)
        editView.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(editable: Editable) {
                addButton.isEnabled = item.validateInputAction(editable)
            }

            override fun beforeTextChanged(sequence: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(sequence: CharSequence, start: Int, before: Int, count: Int) {}
        })
        if (bindFor != item.hashCode() && item.initialData != null) {
            editView.setText(item.initialData.rule)
            editView.setSelection(editView.text.length)
            regexBox.isChecked = item.initialData.regex
            // input is reset on scroll because of rebind, keep it as is
            bindFor = item.hashCode()
        }
    }

    private fun hideKeyboard() {
        itemView.findFocus()?.also {
            val imm = it.context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }
}
