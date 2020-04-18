package koushur.kashmirievents.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "monthData")
data class MonthDataEntity(
    @PrimaryKey
    val Date: String,

    val Events: String
)