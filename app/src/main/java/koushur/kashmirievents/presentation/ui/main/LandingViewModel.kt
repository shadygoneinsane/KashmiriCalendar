package koushur.kashmirievents.presentation.ui.main

import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import koushur.kashmirievents.database.entity.MonthDataEntity
import koushur.kashmirievents.presentation.base.BaseViewModel
import koushur.kashmirievents.repository.CalendarRepository
import javax.inject.Inject

class LandingViewModel @Inject constructor(private val repository: CalendarRepository) :
    BaseViewModel() {

    val monthName = "March"
    var data = MutableLiveData<List<MonthDataEntity>>()

    fun fetchData(jsonData: String) {
        val myType = object : TypeToken<List<MonthDataEntity>>() {}.type
        val data = Gson().fromJson<List<MonthDataEntity>>(jsonData, myType)
        this.data.value = data
        /*repository.fetchMonthDataEntity(monthName)
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
            }.add()*/

    }

    val query: String = ""
}