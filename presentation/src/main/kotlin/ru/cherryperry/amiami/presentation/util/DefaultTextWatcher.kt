package ru.cherryperry.amiami.presentation.util

import android.text.Editable
import android.text.TextWatcher

/**
 * [TextWatcher] with default implementations of it's methods.
 */
abstract class DefaultTextWatcher : TextWatcher {

    override fun afterTextChanged(editable: Editable) {
        // nothing
    }

    override fun beforeTextChanged(charSequence: CharSequence, start: Int, count: Int, after: Int) {
        // nothing
    }

    override fun onTextChanged(charSequence: CharSequence, start: Int, before: Int, count: Int) {
        // nothing
    }
}
