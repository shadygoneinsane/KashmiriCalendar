package koushur.kashmirievents.presentation.utils

import android.view.View
import android.widget.LinearLayout
import androidx.databinding.BindingAdapter
import koushir.kashmirievents.R
import koushur.kashmirievents.data.Importance

object BindingAdapters {
    @JvmStatic
    @BindingAdapter("setColor")
    fun setColor(view: LinearLayout, color: Int?) {
        color?.let {
            view.setBackgroundColor(view.context.getColorCompat(color))
        } ?: view.setBackgroundColor(view.context.getColorCompat(R.color.cv_item_view_bg_color))
    }

    @JvmStatic
    @BindingAdapter("setImp")
    fun setImportance(view: LinearLayout, @Importance imp: Int?) {
        view.setBackgroundColor(
            view.context.getColorCompat(
                when (imp) {
                    Importance.high -> R.color.red_800
                    Importance.med -> R.color.teal_700
                    Importance.low -> R.color.blue_800
                    else -> R.color.blue_800
                }
            )
        )
    }

    @BindingAdapter("app:isVisible")
    fun showHide(view: View, isVisible: Boolean?) {
        view.visibility = if (isVisible != null && isVisible) View.VISIBLE else View.GONE
    }


}