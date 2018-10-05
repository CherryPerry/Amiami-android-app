package ru.cherryperry.amiami.presentation.util

import android.support.v4.view.ViewCompat
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.core.view.updatePadding

/**
 * Fit system window insets to support transparent navigation bar.
 */
fun addPaddingBottomToFitBottomSystemInset(view: View) {
    val padding = view.paddingBottom
    ViewCompat.setOnApplyWindowInsetsListener(view) { insetView, insets ->
        insetView.updatePadding(bottom = padding + insets.systemWindowInsetBottom)
        insets
    }
}

/**
 * Add [TextWatcher] with [TextWatcher.afterTextChanged] implementation.
 */
fun EditText.afterTextChanged(block: (Editable) -> Unit): TextWatcher {
    val watcher = object : DefaultTextWatcher() {
        override fun afterTextChanged(editable: Editable) {
            block(editable)
        }
    }
    this.addTextChangedListener(watcher)
    return watcher
}
