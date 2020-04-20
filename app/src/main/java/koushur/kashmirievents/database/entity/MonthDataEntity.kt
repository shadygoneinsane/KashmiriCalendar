package koushur.kashmirievents.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "monthData")
data class MonthDataEntity(
    @PrimaryKey
    @SerializedName("date")
    val date: String,

    @SerializedName("events")
    val events: String = "",

    @SerializedName("imp")
    val imp: Int = 0
)