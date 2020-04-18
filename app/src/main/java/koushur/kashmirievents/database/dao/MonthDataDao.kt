package koushur.kashmirievents.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import koushur.kashmirievents.database.entity.MonthDataEntity

/**
 * File Description
 *
 * Author: Vikesh Dass
 * Created on: 30-03-2020
 */
@Dao
interface MonthDataDao {
    @Query("SELECT * FROM monthData")
    fun fetchData(): LiveData<MonthDataEntity>
}