package koushur.kashmirievents.repository

import koushur.kashmirievents.database.dao.EventDataDao
import koushur.kashmirievents.database.entity.MonthsDataEntity

class CalendarRepository(private val monthDataDao: EventDataDao) {
    suspend fun fetchMonthDataEntity(month: String): List<MonthsDataEntity> {
        return monthDataDao.fetchData()
    }
}