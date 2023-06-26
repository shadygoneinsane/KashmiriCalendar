package koushur.kashmirievents.database

import androidx.room.Database
import androidx.room.RoomDatabase
import koushur.kashmirievents.database.dao.EventDataDao
import koushur.kashmirievents.database.entity.SavedCalendarEntity

/**
 * Application Database
 */
@Database(
    entities = [SavedCalendarEntity::class],
    version = 1, exportSchema = false
)
abstract class AppDb : RoomDatabase() {

    abstract fun eventDataDao(): EventDataDao
}