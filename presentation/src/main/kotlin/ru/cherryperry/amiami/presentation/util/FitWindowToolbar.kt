package ru.cherryperry.amiami.presentation.util

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.updatePadding

/**
 * [Toolbar] with padding top equals to status bar height.
 */
class FitWindowToolbar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : Toolbar(context, attrs, defStyleAttr) {

    init {
        fitsSystemWindows = true
        ViewCompat.setOnApplyWindowInsetsListener(this) { _, inset ->
            updatePadding(top = inset.systemWindowInsetTop)
            inset.replaceSystemWindowInsets(
                inset.systemWindowInsetLeft, 0,
                inset.systemWindowInsetRight, inset.systemWindowInsetBottom)
        }
    }
}
