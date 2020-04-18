package koushur.kashmirievents.database.dao

import androidx.room.Dao
import androidx.room.Query
import io.reactivex.Single
import koushur.kashmirievents.database.entity.MonthDataEntity

/**
 * File Description
 *
 * Author: Vikesh Dass
 * Created on: 30-03-2020
 */
@Dao
interface MonthDataDao {
    @Query("SELECT * FROM dateEvents")
    fun fetchData(): Single<List<MonthDataEntity>>

}