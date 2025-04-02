package koushur.kashmirievents.utility

import android.content.Context
import android.content.DialogInterface
import android.graphics.drawable.GradientDrawable
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import koushir.kashmirievents.R
import koushur.kashmirievents.KashmiriEventsApplication
import timber.log.Timber


fun View.makeVisible() {
    visibility = View.VISIBLE
}

fun View.makeInVisible() {
    visibility = View.INVISIBLE
}

fun View.makeGone() {
    visibility = View.GONE
}

fun dpToPx(dp: Int, context: Context): Int = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), context.resources.displayMetrics
).toInt()

internal fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}

internal fun Boolean?.orFalse(): Boolean = this ?: false

internal fun Context.getDrawableCompat(@DrawableRes drawable: Int) =
    ContextCompat.getDrawable(this, drawable)

internal fun Context.getColorCompat(@ColorRes color: Int) = ContextCompat.getColor(this, color)

internal fun TextView.setTextColorRes(@ColorRes color: Int) =
    setTextColor(context.getColorCompat(color))

fun GradientDrawable.setCornerRadius(
    topLeft: Float = 0F, topRight: Float = 0F, bottomRight: Float = 0F, bottomLeft: Float = 0F
) {
    cornerRadii = arrayOf(
        topLeft, topLeft, topRight, topRight, bottomRight, bottomRight, bottomLeft, bottomLeft
    ).toFloatArray()
}

fun Int.returnColor(): Int {
    return when (this) {
        Importance.high -> R.color.red_800
        Importance.med -> R.color.teal_700
        Importance.low -> R.color.brown_700
        else -> R.color.brown_700
    }
}

fun String.toast(context: Context?, duration: Int = Toast.LENGTH_SHORT): Toast {
    return Toast.makeText(context, this, duration).apply { show() }
}

fun EditText.resetText() = setText("")


inline fun <reified T> Gson.fromJsonToList(json: String): List<T> {
    val typeToken = object : TypeToken<List<T>>() {}.type
    return this.fromJson(json, typeToken)
}

fun String.materialAlert(context: Context?) {
    context?.let {
        val bld = MaterialAlertDialogBuilder(context, R.style.MaterialAlertDialog)
        bld.setMessage(this)
        bld.setNeutralButton(
            KashmiriEventsApplication.fetchResources().getString(android.R.string.ok), null
        )
        Timber.d("%s %s", "MaterialAlert", "Showing alert dialog: $this")
        bld.create().show()
    }
}

fun Context?.showMaterialAlert(
    title: String? = null,
    message: String? = null,
    positive: String? = null,
    negative: String? = null,
    neutral: String? = null,
    okCallBack: (dialog: DialogInterface, which: Int) -> Unit = { _, _ -> }
) {
    this?.let {
        val builder = MaterialAlertDialogBuilder(it, R.style.MaterialAlertDialog)
        title?.let { builder.setTitle(title) }
        message?.let { builder.setMessage(message) }
        positive?.let {
            builder.setPositiveButton(positive) { dialog, which ->
                okCallBack.invoke(dialog, which)
            }
        }
        negative?.let {
            builder.setNegativeButton(negative) { dialog, which ->
                okCallBack.invoke(dialog, which)
            }
        }
        neutral?.let {
            builder.setNeutralButton(neutral) { dialog, which ->
                okCallBack.invoke(dialog, which)
            }
        }
        Timber.d("%s %s", "MaterialAlert", "Showing alert dialog: $this")
        builder.create().show()
    }
}