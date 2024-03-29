package koushur.kashmirievents.repository

import kotlinx.coroutines.flow.Flow
import koushur.kashmirievents.database.dao.CalendarEventDataDao
import koushur.kashmirievents.database.entity.SavedEventEntity

class CalendarRepository(private val monthDataDao: CalendarEventDataDao) {
    fun fetchAllEvents(): Flow<List<SavedEventEntity>> {
        return monthDataDao.fetchEvents()
    }

    fun saveEvent(calEvent: SavedEventEntity): Unit {
        return monthDataDao.insertEvent(calEvent)
    }
}