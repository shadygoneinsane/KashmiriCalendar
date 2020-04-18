package koushur.kashmirievents.presentation.ui.main

import androidx.lifecycle.MutableLiveData
import koushur.kashmirievents.database.entity.MonthDataEntity
import koushur.kashmirievents.presentation.base.BaseViewModel
import koushur.kashmirievents.presentation.common.ResultState
import koushur.kashmirievents.repository.CalendarRepository
import javax.inject.Inject

class LandingViewModel @Inject constructor(private val repository: CalendarRepository) :
    BaseViewModel() {

    val monthName = "March"
    var data = MutableLiveData<List<MonthDataEntity>>()

    fun fetchData() {
        repository.fetchMonthDataEntity(monthName)
            .toFlowable()
            .subscribe {
                when (it) {
                    is ResultState.Success -> {
                        data.value = it.data
                    }

                    is ResultState.Error -> {
                        val dataA = MonthDataEntity()
                        dataA.events = it.exception.localizedMessage!!
                        data.value = listOf(dataA)
                    }

                }
            }.add()

    }

    val query: String = ""
}