package koushur.kashmirievents.repository

import koushur.kashmirievents.database.dao.MonthDataDao
import koushur.kashmirievents.database.entity.MonthsDataEntity

class CalendarRepository(private val monthDataDao: MonthDataDao) {
    suspend fun fetchMonthDataEntity(month: String): List<MonthsDataEntity> {
        return monthDataDao.fetchData()
    }
}