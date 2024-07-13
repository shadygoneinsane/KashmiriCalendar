package koushur.kashmirievents.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import koushur.kashmirievents.database.entity.SavedEventEntity

/**
 * Dao class for Data
 *
 * Author: Vikesh Dass
 * Created on: 30-03-2020
 * Email : vikeshdass@gmail.com
 */
@Dao
interface CalendarEventDataDao {
    @Query("SELECT * FROM savedEvent")
    fun fetchEvents(): Flow<List<SavedEventEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertEvent(calEventEntity: SavedEventEntity) : Long?

    @Delete
    fun deleteEvent(calEventEntity: SavedEventEntity) : Int?
}