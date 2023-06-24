package koushur.kashmirievents.database.data

import androidx.annotation.ColorRes
import androidx.annotation.IntDef
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
    @ColorRes val color: Int
)

data class MonthEvent(
    val indexOfMonth: Int = -1, val startDate: LocalDate, val endDate: LocalDate,
    val monthName: String, @Importance val imp: Int, @ColorRes val monthColor: Int
) : Event(startDate, eventImp = imp, eventName = monthName, color = monthColor)

data class DayEvent(
    val indexOfDay: Int = -1, val date: LocalDate, val dayName: String,
    @Importance val imp: Int, @ColorRes val dayColor: Int
) : Event(date, eventImp = imp, eventName = dayName, color = dayColor)

@IntDef(Importance.high, Importance.med, Importance.low)
annotation class Importance {
    companion object {
        //red bold
        const val high: Int = 2

        //black bold
        const val med: Int = 1

        //none
        const val low: Int = 0
    }
}