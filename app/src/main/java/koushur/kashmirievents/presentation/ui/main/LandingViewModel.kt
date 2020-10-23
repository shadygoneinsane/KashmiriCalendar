package koushur.kashmirievents.presentation.ui.main

import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kizitonwose.calendarview.model.CalendarMonth
import koushir.kashmirievents.BR
import koushir.kashmirievents.R
import koushur.kashmirievents.data.Event
import koushur.kashmirievents.data.Importance
import koushur.kashmirievents.database.entity.MonthDataEntity
import koushur.kashmirievents.presentation.base.BaseViewModel
import koushur.kashmirievents.presentation.navigation.SingleLiveEvent
import koushur.kashmirievents.presentation.utils.returnColor
import koushur.kashmirievents.repository.CalendarRepository
import me.tatarka.bindingcollectionadapter2.ItemBinding
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter

class LandingViewModel(private val repository: CalendarRepository) :
    BaseViewModel() {

    val monthName = MutableLiveData<String>()
    private lateinit var specialEvents: Map<YearMonth, List<Event>>
    private lateinit var events: Map<LocalDate, List<Event>>
    private lateinit var eventsPerMonth: Map<YearMonth, List<Event>>

    fun getEvents(): Map<LocalDate, List<Event>>? {
        return if (::events.isInitialized)
            events
        else
            null
    }

    fun setMonthName(month: CalendarMonth) {
        val monthTitleFormatter = DateTimeFormatter.ofPattern("MMMM")
        monthName.value = "${monthTitleFormatter.format(month.yearMonth)} ${month.yearMonth.year}"
    }

    val prevMonthEvent = SingleLiveEvent<Void>()
    fun leftClickEvent() {
        prevMonthEvent.call()
    }

    val nextMonthEvent = SingleLiveEvent<Void>()
    fun rightClickEvent() {
        nextMonthEvent.call()
    }


    private fun generateEvents(inputData: List<MonthDataEntity>): List<Event> {
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val list = mutableListOf<Event>()
        inputData.map {
            val todayLocalDate: LocalDateTime = LocalDate.parse(it.date, formatter).atTime(0, 0)
            list.add(Event(todayLocalDate, it.imp, it.events, it.imp.returnColor()))
        }

        return list
    }

    fun fetchEventsData(jsonData: String) {
        val myType = object : TypeToken<List<MonthDataEntity>>() {}.type
        val data = Gson().fromJson<List<MonthDataEntity>>(jsonData, myType)
        if (data.isNotEmpty()) {
            events = generateEvents(data).groupBy { it.time.toLocalDate() }
            eventsPerMonth = generateEvents(data).groupBy {
                YearMonth.of(
                    it.time.toLocalDate().year,
                    it.time.toLocalDate().month
                )
            }
        }
    }

    fun fetchSpecialEventsData(jsonData: String) {
        val myType = object : TypeToken<List<MonthDataEntity>>() {}.type
        val data = Gson().fromJson<List<MonthDataEntity>>(jsonData, myType)
        if (data.isNotEmpty()) {
            specialEvents = generateEvents(data).groupBy { YearMonth.from(it.time.toLocalDate()) }
        }
    }

    val items = ObservableArrayList<Event>()
    fun updateList(date: LocalDate?) {
        items.clear()
        items.addAll(events[date].orEmpty())
    }

    val itemBinding = ItemBinding.of<Event>(BR.event, R.layout.layout_event_item)

    val specialItems = ObservableArrayList<Event>()
    fun updateSpecialItemsList(yearMonth: YearMonth) {
        specialItems.clear()
        specialItems.addAll(specialEvents[yearMonth].orEmpty())

        eventsPerMonth[yearMonth].orEmpty().map {
            if (it.eventImp == Importance.med || it.eventImp == Importance.high) {
                val formatter = DateTimeFormatter.ofPattern("E, MMM dd")
                specialItems.add(
                    Event(
                        it.time, it.eventImp,
                        "On ${it.time.toLocalDate().format(formatter)} -> ${it.eventName} ",
                        it.color
                    )
                )
            }
        }
    }

    val specialItemBinding = ItemBinding.of<Event>(BR.event, R.layout.layout_special_event_item)
}