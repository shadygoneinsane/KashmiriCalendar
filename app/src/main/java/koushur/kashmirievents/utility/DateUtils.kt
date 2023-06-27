package koushur.kashmirievents.utility

import koushur.kashmirievents.database.data.DayEvent
import koushur.kashmirievents.database.entity.Days
import koushur.kashmirievents.database.entity.SpecialDays
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.temporal.WeekFields
import java.util.Locale


const val ddMMyyyy = "dd-MM-yyyy"
val ddMMyyyyFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern(ddMMyyyy)

/**
 * Returns the days of week values such that the desired
 * [firstDayOfWeek] property is at the start position.
 *
 * @see [firstDayOfWeekFromLocale]
 */
fun daysOfWeek(firstDayOfWeek: DayOfWeek = firstDayOfWeekFromLocale()): List<DayOfWeek> {
    val pivot = 7 - firstDayOfWeek.ordinal
    val daysOfWeek = DayOfWeek.values()
    // Order `daysOfWeek` array so that firstDayOfWeek is at the start position.
    return (daysOfWeek.takeLast(pivot) + daysOfWeek.dropLast(pivot))
}

/**
 * Returns the first day of the week from the default locale.
 */
fun firstDayOfWeekFromLocale(): DayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek

fun String.getLocalDate(): LocalDate? = LocalDate.parse(this, ddMMyyyyFormatter)

fun getCurrentDebugTime(): LocalDateTime = LocalDateTime.now()

fun LocalDate.getYearMonth(): YearMonth = YearMonth.of(this.year, this.month)

fun DayEvent.ifDayEventIsHighlighted() = Days.highlights.contains(Days.daysList[indexOfDay])

fun DayEvent.ifDayEventIsSpecial() = SpecialDays.specialDayEvents.keys.contains(eventName)