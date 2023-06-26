package koushur.kashmirievents.database.dao

import androidx.room.Dao
import androidx.room.Query
import koushur.kashmirievents.database.entity.SavedCalendarEntity

/**
 * Dao class for Data
 *
 * Author: Vikesh Dass
 * Created on: 30-03-2020
 * Email : vikeshdass@gmail.com
 */
@Dao
interface EventDataDao {
    @Query("SELECT * FROM savedEvent")
    suspend fun fetchData(): List<SavedCalendarEntity>
}