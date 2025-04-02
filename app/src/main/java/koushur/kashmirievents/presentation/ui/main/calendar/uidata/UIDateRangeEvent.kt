package koushur.kashmirievents.presentation.ui.main.calendar.uidata

import koushur.kashmirievents.database.data.Event
import java.time.LocalDate

data class UIDateRangeEvent(
    val startDate: LocalDate,
    val eventImportance: Int, val eventTitle: String, val eventStart: String, val eventEnd: String
) : Event(startDate, eventTitle, eventImportance)