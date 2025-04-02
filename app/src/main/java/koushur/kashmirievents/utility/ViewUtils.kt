package koushur.kashmirievents.utility

import android.util.TypedValue
import android.view.View
import android.widget.TextView
import koushur.kashmirievents.database.data.DayEvent
import koushur.kashmirievents.database.data.Event


/**
 * Utility for views
 *
 * Created by: Vikesh Dass
 * Created on: 30-10-2020
 * Email : vikeshdass@gmail.com
 */
fun setTVFontColor(tv: TextView, decorView: View, event: DayEvent) {
    decorView.setBackground(event.eventImp)
    val importance = getDayEventImportance(event)
    decorView.makeVisible()
    /*if (importance > Importance.med || event.indexOfDay == -1 || event.ifDayEventIsHighlighted() || event.ifDayEventIsSpecial()) {
        decorView.makeVisible()
    } else {
        decorView.makeGone()
    }*/

    tv.text = event.eventName
    tv.makeVisible()
    tv.setImportance(importance)
}

fun getDayEventImportance(event: Event): Int {
    return if ((event is DayEvent) && (event.indexOfDay != -1) && (event.ifDayEventIsHighlighted())) {
        Importance.med
    } else if ((event is DayEvent) && (event.ifDayEventIsSpecial())) {
        Importance.high
    } else {
        event.eventImp
    }
}

fun setDateDataAndColor(
    events: List<DayEvent>?,
    topTV: TextView, topView: View,
    bottomTV: TextView, bottomView: View,
    dateTV: TextView
) {
    events?.let { eventList ->
        when {
            eventList.count() == 1 -> {
                //log("Date : ${events.first().localDate} | Event found is ${events.first()}")
                val event = eventList.first()
                setTVFontColor(topTV, topView, event)
                topView.setBackground(event.eventImp)
                bottomTV.makeGone()
                bottomView.makeGone()
            }

            eventList.count() == 2 -> {
                dateTV.setTextSizeSp(7f)
                val firstEvent = eventList.first()
                val secondEvent = eventList[1]
                //log("Date : ${events.first().localDate} | Events found are 1: $firstEvent 2: $secondEvent")
                setTVFontColor(topTV, topView, firstEvent)

                setTVFontColor(bottomTV, bottomView, secondEvent)
            }

            eventList.count() > 2 -> {
                dateTV.setTextSizeSp(7f)
                val firstEvent = eventList.first()
                val secondEvent = eventList[1]
                //log("Date : ${events.first().localDate} | Events found are 1: $firstEvent 2: $secondEvent")
                eventList.sortedBy { it.eventImp }
                setTVFontColor(topTV, topView, firstEvent)
                setTVFontColor(bottomTV, bottomView, secondEvent)
            }

            else -> {
                topView.makeGone()
                topTV.makeGone()
                bottomTV.makeGone()
                bottomView.makeGone()
                //log("Date : ${day.date} | Events found $eventList}")
            }
        }
    }
}

fun TextView.setTextSizeSp(size: Float) {
    setTextSize(TypedValue.COMPLEX_UNIT_SP, size)
}

fun View.setOnSingleClickListener(action: (v: View) -> Unit) {
    setOnClickListener { v ->
        isClickable = false
        action(v)
        postDelayed({ isClickable = true }, 1000L)
    }
}