package koushur.kashmirievents.presentation.ui.main.calendar

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.Px
import androidx.core.view.forEach
import androidx.recyclerview.widget.RecyclerView

/**
 * A [RecyclerView.ItemDecoration] which is inset from the left and right by the given amount.
 * Created by: Vikesh Dass
 * Created on: 15-11-2020
 */
class InsetDivider(
    @Px private val inset: Int,
    @Px private val height: Int,
    @ColorInt private val dividerColor: Int
) : RecyclerView.ItemDecoration() {

    private val dividerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = dividerColor
        style = Paint.Style.STROKE
        strokeWidth = height.toFloat()
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val points = mutableListOf<Float>()
        parent.forEach {
            if (parent.getChildAdapterPosition(it) < state.itemCount - 1) {
                val bottom = it.bottom.toFloat()
                points.add((it.left + inset).toFloat())
                points.add(bottom)
                points.add((it.right - inset).toFloat())
                points.add(bottom)
            }
        }
        c.drawLines(points.toFloatArray(), dividerPaint)
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.top = height * 4
        outRect.bottom = height * 4
    }
}