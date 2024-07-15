package koushur.kashmirievents.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import koushur.kashmirievents.database.entity.SavedEventEntity

/**
 * Dao class for Data
 *
 * Author: Vikesh Dass
 * Created on: 27-06-2023
 * Email : vikeshdass@gmail.com
 */
@Dao
interface CalendarEventDataDao {
    @Query("SELECT * FROM savedEvent")
    fun fetchEvents(): List<SavedEventEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertEvent(calEventEntity: SavedEventEntity): Long?

    @Delete
    fun deleteEvent(calEventEntity: SavedEventEntity): Int?
}