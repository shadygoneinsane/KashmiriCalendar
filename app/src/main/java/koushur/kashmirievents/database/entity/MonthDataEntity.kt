package koushur.kashmirievents.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "monthData")
data class MonthDataEntity(
    @PrimaryKey
    val date: String,

    val events: String = "",

    val imp: Int = 0
)