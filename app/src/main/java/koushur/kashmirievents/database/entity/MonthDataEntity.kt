package koushur.kashmirievents.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ScheduleLockProfile")
data class ScheduleLockProfile(
    @PrimaryKey
    val Date: String,

    val Events: String
)