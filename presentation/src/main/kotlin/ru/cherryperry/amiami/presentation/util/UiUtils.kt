package ru.cherryperry.amiami.presentation.util

import android.support.v4.view.ViewCompat
import android.view.View
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
