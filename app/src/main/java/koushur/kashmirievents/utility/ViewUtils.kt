package koushur.kashmirievents.utility

import android.util.TypedValue
import android.view.View
import android.widget.TextView
import koushur.kashmirievents.database.data.DayEvent
import koushur.kashmirievents.database.data.Event
import koushur.kashmirievents.database.entity.Days


/**
 * Utility for views
 *
 * Created by: Vikesh Dass
 * Created on: 30-10-2020
 * Email : vikeshdass@gmail.com
 */
fun setTVFontColor(tv: TextView, decorView: View, event: Event) {
    tv.text = event.eventName
    tv.makeVisible()
    tv.setImportance(event.eventImp)
    decorView.setBackground(event.eventImp)
    decorView.makeVisible()
    if ((event is DayEvent) && (event.indexOfDay != -1)) {
        if (Days.highlights.contains(Days.days[event.indexOfDay]))
            tv.setImportance(Importance.med)
    } else if ((event is DayEvent) && (event.indexOfDay == -1)) {
        tv.setImportance(Importance.high)
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

                setTVFontColor(topTV, topView, eventList.first())
                bottomTV.makeGone()
                bottomView.makeGone()
            }

            eventList.count() == 2 -> {
                dateTV.setTextSizeSp(7f)

                //log("Date : ${day.date} | Events found are 1: ${listEvents[0]} 2: ${listEvents[1]}")
                setTVFontColor(topTV, topView, eventList.first())
                setTVFontColor(bottomTV, bottomView, eventList[1])
            }

            eventList.count() > 2 -> {
                dateTV.setTextSizeSp(7f)

                //log("Date : ${day.date} | Events found are 1: ${listEvents[0]} 2: ${listEvents[1]}")
                eventList.sortedBy { it.eventImp }
                setTVFontColor(topTV, topView, eventList.first())
                setTVFontColor(bottomTV, bottomView, eventList[1])
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