package koushur.kashmirievents.database.entity

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "monthData")
data class MonthDataEntity(

    @PrimaryKey
    @NonNull val date: String = "",

    @NonNull val events: String = ""
)