package koushur.kashmirievents.repository

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import koushur.kashmirievents.database.dao.MonthDataDao
import koushur.kashmirievents.database.entity.MonthDataEntity
import koushur.kashmirievents.presentation.common.ResultState

class CalendarRepository(private val monthDataDao: MonthDataDao) {
    fun fetchMonthDataEntity(month: String): Single<ResultState<List<MonthDataEntity>>> {
        return monthDataDao.fetchData()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                ResultState.Success(it) as ResultState<List<MonthDataEntity>>
            }.onErrorReturn {
                ResultState.Error(it)
            }
    }
}