package ru.cherryperry.amiami.presentation.util

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import ru.cherryperry.amiami.R

/**
 * Simple and effective clip of top with card corner radius.
 * Not necessary to clip bitmap by creating it copy.
 * Backward compatible.
 */
class CardTopImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    private val clipRect = RectF()
    private val clipPath = Path()
    private val radius = resources.getDimension(R.dimen.corner_radius_card)
    private val radii = floatArrayOf(radius, radius, radius, radius, 0f, 0f, 0f, 0f)

    override fun onDraw(canvas: Canvas) {
        if (clipRect.width().toInt() != canvas.width || clipRect.height().toInt() != canvas.height) {
            clipRect.set(0f, 0f, this.width.toFloat(), this.height.toFloat())
            clipPath.reset()
            clipPath.addRoundRect(clipRect, radii, Path.Direction.CW)
        }
        canvas.clipPath(clipPath)
        super.onDraw(canvas)
    }
}
