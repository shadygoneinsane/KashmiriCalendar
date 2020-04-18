package koushur.kashmirievents.repository

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import koushur.kashmirievents.database.dao.MonthDataDao
import koushur.kashmirievents.database.entity.MonthDataEntity
import javax.inject.Inject

class CalendarRepository @Inject constructor(private val monthDataDao: MonthDataDao) {
    fun fetchMonthDataEntity(month: String): Single<List<MonthDataEntity>> {
        return monthDataDao.fetchData()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}