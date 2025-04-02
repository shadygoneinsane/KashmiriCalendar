package koushur.kashmirievents.presentation.ui.main.calendar.uidata

import koushur.kashmirievents.database.data.Event
import java.time.LocalDate

data class UIDateEvent(
    val startDate: LocalDate,
    val eventImportance: Int,
    val eventTitle: String,
    val eventDate: String
) : Event(startDate, eventTitle, eventImportance)