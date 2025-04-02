package koushur.kashmirievents.database.entity

import com.google.gson.annotations.SerializedName
import koushur.kashmirievents.database.data.DayEvent
import koushur.kashmirievents.utility.DateUtils
import java.time.LocalDate

data class DayDataEntity(
    @SerializedName("date") val date: String,

    @SerializedName("events") val eventName: String = "",

    @SerializedName("imp") val imp: Int = 0
)

/**
 *  For generating list<[DayEvent]> from list<[DayDataEntity]>
 *  since [DayDataEntity] has date as [String] but we want [LocalDate] hence, using [DayEvent]
 */
fun List<DayDataEntity>.map(): List<DayEvent> {
    val list = this.map { dayEntity ->
        val indexFromList = Days.daysList.indexOf(dayEntity.eventName.matchDayNameFromConstants())
        DayEvent(
            indexOfDay = indexFromList,
            date = LocalDate.parse(dayEntity.date, DateUtils.ddMMyyyyFormatter),
            dayName = dayEntity.eventName,
            imp = dayEntity.imp
        )
    }

    return list
}

fun String.matchDayNameFromConstants(): String {
    return Days.daysList.find { contains(it) } ?: ""
}