package koushur.kashmirievents.utility

import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RoundRectShape
import android.view.View
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import koushir.kashmirievents.R
import koushur.kashmirievents.database.data.Importance


@BindingAdapter("setColor")
fun setColor(view: LinearLayout, color: Int?) {
    color?.let {
        view.setBackgroundColor(view.context.getColorCompat(color))
    } ?: view.setBackgroundColor(view.context.getColorCompat(R.color.cv_item_view_bg_color))
}

@BindingAdapter("setImportance")
fun setImportance(view: ConstraintLayout, @Importance imp: Int?) {
    val r = 8f
    val shape = ShapeDrawable(RoundRectShape(floatArrayOf(r, r, r, r, r, r, r, r), null, null))
    val color = view.context.getColorCompat(
        when (imp) {
            Importance.high -> R.color.red_800
            Importance.med -> R.color.teal_700
            Importance.low -> R.color.blue_800
            else -> R.color.blue_800
        }
    )
    shape.paint.color = color
    view.background = shape
}

@BindingAdapter("isVisible")
fun showHide(view: View, isVisible: Boolean?) {
    view.visibility = if (isVisible != null && isVisible) View.VISIBLE else View.GONE
}