package android.support.design.widget

import android.content.Context
import android.support.v4.view.ViewCompat
import android.util.AttributeSet

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
