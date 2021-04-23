package koushur.kashmirievents.presentation.ui.main.calendar

import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import koushir.kashmirievents.BR
import koushir.kashmirievents.R
import koushur.kashmirievents.data.Event
import koushur.kashmirievents.data.Importance
import koushur.kashmirievents.database.entity.MonthDataEntity
import koushur.kashmirievents.presentation.base.BaseViewModel
import koushur.kashmirievents.presentation.navigation.SingleLiveEvent
import koushur.kashmirievents.presentation.utils.returnColor
import me.tatarka.bindingcollectionadapter2.ItemBinding
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

class LandingViewModel : BaseViewModel() {
    private val listMonthDataEntityType = object : TypeToken<List<MonthDataEntity>>() {}.type
    private val monthNameLiveData = MutableLiveData<String>()

    private var eventsLiveDataLiveData = MutableLiveData<MutableList<Event>>(mutableListOf())
    private var eventsPerMonthMap: Map<YearMonth, List<Event>> = emptyMap()

    private val prevMonthEvent = SingleLiveEvent<Void>()
    private val nextMonthEvent = SingleLiveEvent<Void>()

    private val specialEventsMap: MutableMap<YearMonth, List<Event>> = mutableMapOf()
    val specialItems = ObservableArrayList<Event>()
    val selectedDayItemBinding = ItemBinding.of<Event>(BR.event, R.layout.layout_event_item)

    val selectedDayItems = ObservableArrayList<Event>()
    val specialItemBinding = ItemBinding.of<Event>(BR.event, R.layout.layout_special_event_item)

    fun getMonthName() = monthNameLiveData
    fun getEventsLiveData() = eventsLiveDataLiveData
    fun getNextMonthClickEvent() = nextMonthEvent
    fun getPrevMonthClickEvent() = prevMonthEvent

    private fun setMonthName(yearMonth: YearMonth) {
        val monthTitleFormatter = DateTimeFormatter.ofPattern("MMMM")
        monthNameLiveData.value =
            "${monthTitleFormatter.format(yearMonth)} ${yearMonth.year}"
    }

    fun leftClickEvent() {
        prevMonthEvent.call()
    }

    fun rightClickEvent() {
        nextMonthEvent.call()
    }

    /**
     *  For generating list<Event> from list<MonthDataEntity>
     */
    private fun generateEvents(inputData: List<MonthDataEntity>): List<Event> {
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val list = mutableListOf<Event>()
        inputData.map {
            val todayLocalDate: LocalDate = LocalDate.parse(it.date, formatter)
            list.add(Event(todayLocalDate, it.imp, it.events, it.imp.returnColor()))
        }

        return list
    }

    fun fetchEventsData(allEvents: String, eventsLiveData: MutableLiveData<MutableList<Event>>) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val data: List<MonthDataEntity> =
                    Gson().fromJson(allEvents, listMonthDataEntityType)
                val list: MutableList<Event>? = eventsLiveData.value
                list?.addAll(generateEvents(data))
                eventsLiveData.postValue(list)
            }
        }
    }

    fun setEventsData(data: List<Event>): Map<LocalDate, List<Event>> {
        return if (data.isNotEmpty()) {
            eventsPerMonthMap = data.groupBy { YearMonth.of(it.localDate.year, it.localDate.month) }
            data.groupBy { it.localDate }
        } else emptyMap()
    }

    fun fetchSpecialEventsData(specialEvents: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val data: List<MonthDataEntity> =
                    Gson().fromJson(specialEvents, listMonthDataEntityType)
                if (data.isNotEmpty())
                    this@LandingViewModel.specialEventsMap.putAll(
                        generateEvents(data).groupBy { YearMonth.from(it.localDate) }
                    )
            }
        }
    }

    fun updateSelectedDayItems(events: List<Event>?) {
        selectedDayItems.clear()
        selectedDayItems.addAll(events.orEmpty())
    }

    fun updateSpecialItemsList(yearMonth: YearMonth) {
        setMonthName(yearMonth)
        specialItems.clear()
        specialItems.addAll(specialEventsMap[yearMonth].orEmpty())

        eventsPerMonthMap[yearMonth].orEmpty().map {
            if (it.eventImp == Importance.med || it.eventImp == Importance.high) {
                val formatter = DateTimeFormatter.ofPattern("E, MMM dd")
                specialItems.add(
                    Event(
                        localDate = it.localDate, eventImp = it.eventImp,
                        eventName = "On ${it.localDate.format(formatter)} - ${it.eventName} ",
                        color = it.color
                    )
                )
            }
        }
    }
}