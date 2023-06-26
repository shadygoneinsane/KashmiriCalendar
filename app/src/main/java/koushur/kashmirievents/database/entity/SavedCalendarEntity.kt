package koushur.kashmirievents.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "savedEvent")
data class SavedCalendarEntity(
    @PrimaryKey val id: Int,

    val selectedDate: LocalDate,

    val monthIndex: Int,

    val monthName: String,

    val dayIndex: Int,

    val dayName: String,

    val eventName: String
)