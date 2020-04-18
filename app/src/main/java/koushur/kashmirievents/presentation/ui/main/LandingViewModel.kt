package koushur.kashmirievents.presentation.ui.main

import androidx.lifecycle.LiveData
import koushur.kashmirievents.database.entity.MonthDataEntity
import koushur.kashmirievents.presentation.base.BaseViewModel
import koushur.kashmirievents.repository.CalendarRepository
import javax.inject.Inject

class LandingViewModel @Inject constructor(private val repository: CalendarRepository) :
    BaseViewModel() {

    val monthName = "March"
    var data: LiveData<MonthDataEntity> = repository.fetchMonthDataEntity(monthName)

    fun fetchData(){
        data.value
    }
}