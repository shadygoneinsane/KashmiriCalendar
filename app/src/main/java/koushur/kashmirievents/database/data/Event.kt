package koushur.kashmirievents.database.data

import koushur.kashmirievents.utility.Importance
import java.time.LocalDate


/**
 * Data class to be used for each events
 *
 * Author: Vikesh Dass
 * Created on: 18-04-2020
 * Email : vikeshdass@gmail.com
 */
open class Event(
    val localDate: LocalDate,
    val eventName: String,
    @Importance val eventImp: Int,
)

data class MonthEvent(
    val indexOfMonth: Int = -1, val startDate: LocalDate, val endDate: LocalDate,
    val monthName: String, @Importance val imp: Int
) : Event(startDate, eventImp = imp, eventName = monthName)

data class DayEvent(
    val indexOfDay: Int = -1, val date: LocalDate, val dayName: String,
    @Importance val imp: Int
) : Event(date, eventImp = imp, eventName = dayName)