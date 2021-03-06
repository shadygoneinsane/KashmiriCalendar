package koushur.kashmirievents.presentation.ui.main.calendar

import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kizitonwose.calendarview.model.CalendarMonth
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
    private val monthName = MutableLiveData<String>()

    private var eventsLiveData = MutableLiveData<List<Event>>(listOf())
    private var eventsPerMonth: Map<YearMonth, List<Event>> = emptyMap()

    private val prevMonthEvent = SingleLiveEvent<Void>()
    private val nextMonthEvent = SingleLiveEvent<Void>()

    private lateinit var specialEvents: Map<YearMonth, List<Event>>
    val specialItems = ObservableArrayList<Event>()
    val selectedDayItemBinding = ItemBinding.of<Event>(BR.event, R.layout.layout_event_item)

    val selectedDayItems = ObservableArrayList<Event>()
    val specialItemBinding = ItemBinding.of<Event>(BR.event, R.layout.layout_special_event_item)

    fun getMonthName() = monthName
    fun getEventsLiveData() = eventsLiveData
    fun getNextMonthClickEvent() = nextMonthEvent
    fun getPrevMonthClickEvent() = prevMonthEvent

    fun setMonthName(month: CalendarMonth) {
        val monthTitleFormatter = DateTimeFormatter.ofPattern("MMMM")
        monthName.value = "${monthTitleFormatter.format(month.yearMonth)} ${month.yearMonth.year}"
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

    fun fetchEventsData(allEvents: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val data: List<MonthDataEntity> =
                    Gson().fromJson(allEvents, listMonthDataEntityType)
                eventsLiveData.postValue(generateEvents(data))
            }
        }
    }

    fun setEventsData(data: List<Event>): Map<LocalDate, List<Event>> {
        return if (data.isNotEmpty()) {
            eventsPerMonth = data.groupBy { YearMonth.of(it.localDate.year, it.localDate.month) }
            data.groupBy { it.localDate }
        } else emptyMap()
    }

    fun fetchSpecialEventsData(specialEvents: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val data: List<MonthDataEntity> =
                    Gson().fromJson(specialEvents, listMonthDataEntityType)
                if (data.isNotEmpty())
                    this@LandingViewModel.specialEvents =
                        generateEvents(data).groupBy { YearMonth.from(it.localDate) }
            }
        }
    }

    fun updateSelectedDayItems(events: List<Event>?) {
        selectedDayItems.clear()
        selectedDayItems.addAll(events.orEmpty())
    }

    fun updateSpecialItemsList(month: CalendarMonth) {
        setMonthName(month)
        val yearMonth = month.yearMonth
        specialItems.clear()
        specialItems.addAll(specialEvents[yearMonth].orEmpty())

        eventsPerMonth[yearMonth].orEmpty().map {
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