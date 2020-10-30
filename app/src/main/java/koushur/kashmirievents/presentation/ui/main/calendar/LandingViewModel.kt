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

    private lateinit var events: Map<LocalDate, List<Event>>
    private lateinit var eventsPerMonth: Map<YearMonth, List<Event>>

    private val prevMonthEvent = SingleLiveEvent<Void>()
    private val nextMonthEvent = SingleLiveEvent<Void>()

    private lateinit var specialEvents: Map<YearMonth, List<Event>>
    val specialItems = ObservableArrayList<Event>()
    val selectedDayItemBinding = ItemBinding.of<Event>(BR.event, R.layout.layout_event_item)

    val selectedDayItems = ObservableArrayList<Event>()
    val specialItemBinding = ItemBinding.of<Event>(BR.event, R.layout.layout_special_event_item)

    fun getMonthName() = monthName
    fun getNextMonthClickEvent() = nextMonthEvent
    fun getPrevMonthClickEvent() = prevMonthEvent

    fun getEvents(date: LocalDate): List<Event>? {
        return if (::events.isInitialized) events[date]
        else null
    }

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
                if (data.isNotEmpty()) {
                    events = generateEvents(data).groupBy { it.time }
                    eventsPerMonth = generateEvents(data)
                        .groupBy { YearMonth.of(it.time.year, it.time.month) }
                }
            }
        }
    }

    fun fetchSpecialEventsData(specialEvents: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val data: List<MonthDataEntity> =
                    Gson().fromJson(specialEvents, listMonthDataEntityType)
                if (data.isNotEmpty())
                    this@LandingViewModel.specialEvents =
                        generateEvents(data).groupBy { YearMonth.from(it.time) }
            }
        }
    }

    fun updateSelectedDayItems(date: LocalDate?) {
        selectedDayItems.clear()
        selectedDayItems.addAll(events[date].orEmpty())
    }

    fun updateSpecialItemsList(yearMonth: YearMonth) {
        specialItems.clear()
        specialItems.addAll(specialEvents[yearMonth].orEmpty())

        eventsPerMonth[yearMonth].orEmpty().map {
            if (it.eventImp == Importance.med || it.eventImp == Importance.high) {
                val formatter = DateTimeFormatter.ofPattern("E, MMM dd")
                specialItems.add(
                    Event(
                        time = it.time, eventImp = it.eventImp,
                        eventName = "On ${it.time.format(formatter)} - ${it.eventName} ",
                        color = it.color
                    )
                )
            }
        }
    }
}