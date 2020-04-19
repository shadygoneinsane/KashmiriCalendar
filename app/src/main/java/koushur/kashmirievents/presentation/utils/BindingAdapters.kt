package koushur.kashmirievents.presentation.utils

import android.view.View
import android.widget.LinearLayout
import androidx.databinding.BindingAdapter
import koushir.kashmirievents.R

object BindingAdapters {
    @JvmStatic
    @BindingAdapter("setColor")
    fun setProgressBarVisibility(view: LinearLayout, color: Int?) {
        color?.let {
            view.setBackgroundColor(view.context.getColorCompat(color))
        } ?: view.setBackgroundColor(view.context.getColorCompat(R.color.cv_item_view_bg_color))
    }

    @BindingAdapter("app:isVisible")
    fun showHide(view: View, isVisible: Boolean?) {
        view.visibility = if (isVisible != null && isVisible) View.VISIBLE else View.GONE
    }


}