package koushur.kashmirievents.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import koushur.kashmirievents.database.data.MonthEvent
import koushur.kashmirievents.utility.DateUtils
import koushur.kashmirievents.utility.Importance
import java.time.LocalDate

@Entity(tableName = "monthData")
data class MonthsDataEntity(
    @PrimaryKey
    @SerializedName("startDate")
    val startDate: String,

    @SerializedName("endDate")
    val endDate: String,

    @SerializedName("monthName")
    val monthName: String = "",

    @SerializedName("imp")
    @Importance val importance: Int = 0
)


/**
 *  For generating list<[MonthEvent]> from list<[MonthsDataEntity]>
 *  since [MonthsDataEntity] has date as [String] but we want [LocalDate] hence, using [MonthEvent]
 */
fun List<MonthsDataEntity>.map(): List<MonthEvent> {
    return map { monthEntity: MonthsDataEntity ->
        val indexFromList = Months.monthsList.indexOf(monthEntity.monthName)
        MonthEvent(
            indexFromList, LocalDate.parse(monthEntity.startDate, DateUtils.ddMMyyyyFormatter),
            LocalDate.parse(monthEntity.endDate, DateUtils.ddMMyyyyFormatter),
            monthEntity.monthName, monthEntity.importance
        )
    }
}