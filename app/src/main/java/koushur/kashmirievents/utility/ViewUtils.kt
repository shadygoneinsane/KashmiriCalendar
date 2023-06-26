package koushur.kashmirievents.utility

import android.util.TypedValue
import android.view.View
import android.widget.TextView
import koushur.kashmirievents.database.data.DayEvent
import koushur.kashmirievents.database.data.Event
import koushur.kashmirievents.database.entity.Days
import koushur.kashmirievents.database.entity.SpecialDays


/**
 * Utility for views
 *
 * Created by: Vikesh Dass
 * Created on: 30-10-2020
 * Email : vikeshdass@gmail.com
 */
fun setTVFontColor(tv: TextView, decorView: View, event: Event) {
    decorView.setBackground(event.eventImp)
    decorView.makeVisible()

    tv.text = event.eventName
    tv.makeVisible()
    tv.setImportance(getTVFontColor(event))
}

fun getTVFontColor(event: Event): Int {
    return if ((event is DayEvent) && (event.indexOfDay != -1) && (Days.highlights.contains(Days.daysList[event.indexOfDay]))) {
        Importance.med
    } else if ((event is DayEvent) && (SpecialDays.specialDayEvents.keys.contains(event.eventName))) {
        Importance.high
    } else {
        event.eventImp
    }
}

fun setDateDataAndColor(
    events: List<Event>?,
    topTV: TextView, topView: View,
    bottomTV: TextView, bottomView: View,
    dateTV: TextView
) {
    events?.let { eventList ->
        when {
            eventList.count() == 1 -> {
                //log("Date : ${day.date} | Event found is ${listEvents[0]}")
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
                //log("Date : ${day.date} | Events found are 1: ${firstEvent} 2: ${secondEvent}")
                setTVFontColor(topTV, topView, firstEvent)

                setTVFontColor(bottomTV, bottomView, secondEvent)
            }

            eventList.count() > 2 -> {
                dateTV.setTextSizeSp(7f)
                val firstEvent = eventList.first()
                val secondEvent = eventList[1]
                //log("Date : ${day.date} | Events found are 1: ${firstEvent} 2: ${secondEvent}")
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