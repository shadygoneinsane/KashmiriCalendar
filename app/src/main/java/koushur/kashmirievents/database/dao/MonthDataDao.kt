package koushur.kashmirievents.database.dao

import androidx.room.Dao
import androidx.room.Query
import koushur.kashmirievents.database.entity.MonthDataEntity

/**
 * Dao class for Data
 *
 * Author: Vikesh Dass
 * Created on: 30-03-2020
 * Email : vikeshdass@gmail.com
 */
@Dao
interface MonthDataDao {
    @Query("SELECT * FROM monthData")
    suspend fun fetchData(): List<MonthDataEntity>
}