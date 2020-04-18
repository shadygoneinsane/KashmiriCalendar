package koushur.kashmirievents.database

import androidx.room.Database
import androidx.room.RoomDatabase
import koushur.kashmirievents.database.dao.MonthDataDao
import koushur.kashmirievents.database.entity.MonthDataEntity

/**
 * Application Database
 */
@Database(
    entities = [MonthDataEntity::class],
    version = 1, exportSchema = false
)
abstract class AppDb : RoomDatabase() {

    abstract fun monthDataDao(): MonthDataDao

}