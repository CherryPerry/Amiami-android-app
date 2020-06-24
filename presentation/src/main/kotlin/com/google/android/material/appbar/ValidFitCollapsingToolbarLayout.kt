package com.google.android.material.appbar

import android.content.Context
import android.util.AttributeSet
import androidx.core.view.ViewCompat

/**
 * [CollapsingToolbarLayout] consumes all insets, this is wrong!
 * Bottom inset for navigation panel is lost.
 */
class ValidFitCollapsingToolbarLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : CollapsingToolbarLayout(context, attrs, defStyleAttr) {

    init {
        ViewCompat.setOnApplyWindowInsetsListener(this) { _, insets ->
            onWindowInsetChanged(insets)
            insets.replaceSystemWindowInsets(
                insets.systemWindowInsetLeft, 0,
                insets.systemWindowInsetRight, insets.systemWindowInsetBottom)
        }
    }
}
