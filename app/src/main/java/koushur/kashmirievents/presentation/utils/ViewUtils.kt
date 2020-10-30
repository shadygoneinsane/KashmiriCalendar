package koushur.kashmirievents.presentation.utils

import android.graphics.Typeface
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import com.kizitonwose.calendarview.model.CalendarDay
import koushir.kashmirievents.R
import koushur.kashmirievents.data.Event
import koushur.kashmirievents.data.Importance
import koushur.kashmirievents.presentation.ui.main.calendar.LandingFragment
import timber.log.Timber
import java.time.LocalDate


/**
 * Utility for views
 *
 * Created by: Vikesh Dass
 * Created on: 30-10-2020
 * Email : vikeshdass@gmail.com
 */
fun setTVFontColor(tv: TextView, decorView: View, event: Event) {
    tv.text = event.eventName
    decorView.setBackgroundColor(tv.context.getColorCompat(event.color))

    tv.makeVisible()
    decorView.makeVisible()

    when (event.eventImp) {
        Importance.high -> {
            tv.setTypeface(Typeface.DEFAULT, Typeface.BOLD)
            tv.setTextColor(tv.context.getColorCompat(R.color.red_800))
        }
        Importance.med -> {
            tv.setTypeface(Typeface.DEFAULT, Typeface.BOLD)
            tv.setTextColor(tv.context.getColorCompat(R.color.white))
        }
        Importance.low -> {
            tv.setTypeface(Typeface.DEFAULT, Typeface.NORMAL)
            tv.setTextColor(tv.context.getColorCompat(R.color.white))
        }
        else -> tv.setTextColor(tv.context.getColorCompat(R.color.white))
    }
}

fun setDateDataAndColor(
    selectedDate: LocalDate?,
    events: List<Event>?,
    container: LandingFragment.DayViewContainer,
    day: CalendarDay
) {
    container.dateTV.setTextColorRes(R.color.cv_text_grey)
    //highlighting today's date
    container.layout.setBackgroundResource(
        if (day.date != LocalDate.now()) {
            if (selectedDate == day.date) R.drawable.drawable_selected_bg
            else 0
        } else R.drawable.drawable_selected_highlighted_bg
    )

    events?.let { listTodaysEvents ->
        when {
            listTodaysEvents.count() == 1 -> {
                //Timber.d("Date : ${day.date} | Event found is ${listEvents[0]}")

                setTVFontColor(container.topTV, container.topView, listTodaysEvents[0])
                container.bottomTV.makeGone()
                container.bottomView.makeGone()
            }
            listTodaysEvents.count() == 2 -> {
                container.dateTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 8f)

                //Timber.d("Date : ${day.date} | Events found are 1: ${listEvents[0]} 2: ${listEvents[1]}")
                setTVFontColor(container.topTV, container.topView, listTodaysEvents[0])
                setTVFontColor(container.bottomTV, container.bottomView, listTodaysEvents[1])
            }
            else -> {
                container.topView.makeGone()
                container.topTV.makeGone()
                container.bottomTV.makeGone()
                container.bottomView.makeGone()
                Timber.tag("NoEventFound")
                    .d("Date : ${day.date} | Events found $listTodaysEvents}")
            }
        }
    }
}