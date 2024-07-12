package koushur.kashmirievents.utility

import android.graphics.Typeface
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RoundRectShape
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.databinding.BindingAdapter
import koushir.kashmirievents.R


@BindingAdapter("setColor")
fun View.setBackground(@Importance imp: Int?) {
    val r = 8f
    val shape = ShapeDrawable(RoundRectShape(floatArrayOf(r, r, r, r, r, r, r, r), null, null))
    val color = context.getColorCompat(
        when (imp) {
            Importance.high -> R.color.red_800
            Importance.med -> R.color.teal_700
            Importance.low -> R.color.brown_700
            else -> R.color.brown_700
        }
    )
    shape.paint.color = color
    background = shape
}

@BindingAdapter("setImportance")
fun View.setImportance(@Importance imp: Int?) {
    val r = 24f
    val shape = ShapeDrawable(RoundRectShape(floatArrayOf(r, r, r, r, r, r, r, r), null, null))
    val color = context.getColorCompat(
        when (imp) {
            Importance.high -> R.color.red_800
            Importance.med -> R.color.teal_700
            Importance.low -> R.color.royal_blue
            else -> R.color.brown_700
        }
    )
    shape.paint.color = color
    background = shape
}

fun TextView.setImportance(@Importance imp: Int?) {
    @ColorRes val color: Int
    val typeFace: Int
    when (imp) {
        Importance.high -> {
            color = R.color.red_800
            typeFace = Typeface.BOLD
        }

        Importance.med -> {
            color = R.color.white
            typeFace = Typeface.BOLD
        }

        Importance.low -> {
            color = R.color.white
            typeFace = Typeface.NORMAL
        }

        else -> {
            color = R.color.white
            typeFace = Typeface.NORMAL
        }
    }
    setTextColor(context.getColorCompat(color))
    setTypeface(Typeface.DEFAULT, typeFace)
}
@BindingAdapter("isVisible")
fun showHide(view: View, isVisible: Boolean?) {
    view.visibility = if (isVisible != null && isVisible) View.VISIBLE else View.GONE
}