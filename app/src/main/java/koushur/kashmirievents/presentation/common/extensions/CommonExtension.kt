package koushur.kashmirievents.presentation.common.extensions

import android.content.Context
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

fun String.toast(context: Context?, duration: Int = Toast.LENGTH_SHORT): Toast {
    return Toast.makeText(context, this, duration).apply { show() }
}

fun <T> LifecycleOwner.observe(liveData: LiveData<T>, action: (t: T) -> Unit) {
    liveData.observe(this, Observer { it?.let { t -> action(t) } })
}

fun EditText.resetText() = setText("")

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}
