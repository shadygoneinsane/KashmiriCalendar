package koushur.kashmirievents.repository

import androidx.lifecycle.LiveData
import koushur.kashmirievents.database.dao.MonthDataDao
import koushur.kashmirievents.database.entity.MonthDataEntity
import javax.inject.Inject

class CalendarRepository @Inject constructor(private val monthDataDao: MonthDataDao) {
    fun fetchMonthDataEntity(month: String): LiveData<MonthDataEntity> {
        return monthDataDao.fetchData()
    }
}