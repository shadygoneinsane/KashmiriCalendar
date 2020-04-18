package koushur.kashmirievents.presentation.ui.main

import androidx.lifecycle.MutableLiveData
import koushur.kashmirievents.database.entity.MonthDataEntity
import koushur.kashmirievents.presentation.base.BaseViewModel
import koushur.kashmirievents.repository.CalendarRepository
import javax.inject.Inject

class LandingViewModel @Inject constructor(private val repository: CalendarRepository) :
    BaseViewModel() {

    val monthName = "March"
    var data = MutableLiveData<List<MonthDataEntity>>()

    fun fetchData() {
        repository.fetchMonthDataEntity(monthName)
            .subscribe({
                data.value = it
            }, {
            }).add()

    }

    val query: String = ""
}