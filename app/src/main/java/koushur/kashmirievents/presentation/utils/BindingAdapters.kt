package koushur.kashmirievents.presentation.utils

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter

object BindingAdapters {
    @JvmStatic
    @BindingAdapter("app:task_completion_text")
    fun setProgressBarVisibility(textView: TextView, isCompleted: Boolean?) {
        isCompleted?.let {
            val status = "Completed: $isCompleted"
            textView.text = status
        } ?: run { textView.visibility = View.GONE }
    }

    @JvmStatic
    @BindingAdapter("app:isVisible")
    fun showHide(view: View, isVisible: Boolean?) {
        view.visibility = if (isVisible != null && isVisible) View.VISIBLE else View.GONE
    }

    @JvmStatic
    @BindingAdapter("app:showDate")
    fun showDate(view: TextView, date: Long?) {
        date?.let {
            view.text = DateUtils.getDateAsString(date)
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["minTemp", "maxTemp"], requireAll = true)
    fun setTempString(
        view: TextView,
        minTemp: Float,
        maxTemp: Float
    ) {
        val temp: String = "Temp: $minTemp - $maxTemp"
        view.text = temp
    }

    @JvmStatic
    @BindingAdapter(value = ["firstName", "secondName"], requireAll = false)
    fun setCityNameString(
        view: TextView,
        firstName: String?,
        secondName: String?
    ) {
        firstName?.let {
            view.text = it
        }
        secondName?.let {
            view.text = it
        }

    }

    @JvmStatic
    @BindingAdapter("humidity")
    fun setHumidityString(
        view: TextView,
        humidity: Double
    ) {
        val temp: String = "Humidity: $humidity"
        view.text = temp
    }

    @JvmStatic
    @BindingAdapter("pressure")
    fun setPressureString(
        view: TextView,
        humidity: Double
    ) {
        val temp: String = "Pressure: $humidity"
        view.text = temp
    }

}