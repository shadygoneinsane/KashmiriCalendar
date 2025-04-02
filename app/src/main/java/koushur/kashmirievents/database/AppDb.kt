package koushur.kashmirievents.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import koushur.kashmirievents.database.convertor.Converters
import koushur.kashmirievents.database.dao.CalendarEventDataDao
import koushur.kashmirievents.database.entity.SavedEventEntity

/**
 * Application Database
 */
@Database(
    entities = [SavedEventEntity::class],
    version = 2, exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDb : RoomDatabase() {

    abstract fun eventsDataDao(): CalendarEventDataDao
}