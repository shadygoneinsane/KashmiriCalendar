package koushur.kashmirievents.presentation.ui.main.calendar

import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import koushir.kashmirievents.BR
import koushir.kashmirievents.R
import koushur.kashmirievents.database.data.DayEvent
import koushur.kashmirievents.database.data.Event
import koushur.kashmirievents.database.data.Importance
import koushur.kashmirievents.database.data.MonthEvent
import koushur.kashmirievents.database.entity.DayDataEntity
import koushur.kashmirievents.database.entity.MonthsDataEntity
import koushur.kashmirievents.database.entity.map
import koushur.kashmirievents.presentation.base.BaseViewModel
import koushur.kashmirievents.presentation.navigation.SingleLiveEvent
import koushur.kashmirievents.utility.log
import me.tatarka.bindingcollectionadapter2.itembindings.OnItemBindClass
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

class LandingViewModel : BaseViewModel() {
    private val ioDispatcher = Dispatchers.IO
    private val listDayDataEntityType = object : TypeToken<List<DayDataEntity>>() {}.type
    private val monthNameLiveData = MutableLiveData<String>()
    private val monthDataEntityType = object : TypeToken<List<MonthsDataEntity>>() {}.type
    private var listOfMonthEvents = mutableListOf<MonthEvent>()

    /**
     * Used to store list of all events day wise, in [Event] format
     */
    private val daysEventLiveData = MutableLiveData<MutableList<DayEvent>>(mutableListOf())

    private var dayEventsPerMonthMap: Map<YearMonth, List<Event>> = emptyMap()

    /**
     * Used to populate list of items for current month on UI
     */
    private val monthEventsMap: MutableMap<YearMonth, List<MonthEvent>> = mutableMapOf()

    private val prevMonthEvent = SingleLiveEvent<Unit?>()
    private val nextMonthEvent = SingleLiveEvent<Unit?>()

    val monthlyItems = ObservableArrayList<Any>()
    val monthlyItemBinding: OnItemBindClass<Any> = OnItemBindClass<Any>()
        .map(Event::class.java, BR.event, R.layout.layout_special_event_item)

    val selectedDayItems = ObservableArrayList<Any>()
    val selectedDayItemBinding: OnItemBindClass<Any> = OnItemBindClass<Any>()
        .map(DayEvent::class.java, BR.event, R.layout.layout_special_event_item)
        .map(Event::class.java, BR.event, R.layout.layout_special_event_item)


    fun getMonthName() = monthNameLiveData
    fun getDayEventsLiveData() = daysEventLiveData
    fun getNextMonthClickEvent() = nextMonthEvent
    fun getPrevMonthClickEvent() = prevMonthEvent

    fun setMonthName(yearMonth: YearMonth) {
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

    fun fetchDaysEventsData(allEvents: List<String?>) {
        fetchDaysEventsData(allEvents, daysEventLiveData)
    }

    private fun fetchDaysEventsData(
        allDaysEvents: List<String?>, dayWiseEvents: MutableLiveData<MutableList<DayEvent>>
    ) {
        val list: MutableList<DayEvent> = dayWiseEvents.value ?: mutableListOf()
        execute(ioDispatcher) {
            allDaysEvents.forEach { allDayData ->
                allDayData?.let {
                    val data: List<DayDataEntity> =
                        Gson().fromJson(allDayData, listDayDataEntityType)
                    list.addAll(data.map())


                    dayWiseEvents.postValue(list)
                }
            }
        }
    }

    fun setMonthWiseDayEventData(data: List<DayEvent>): Map<LocalDate, List<DayEvent>> {
        return if (data.isNotEmpty()) {
            dayEventsPerMonthMap =
                data.groupBy { YearMonth.of(it.localDate.year, it.localDate.month) }
            data.groupBy { it.localDate }
        } else emptyMap()
    }

    fun fetchMonthsEventsData(months: List<String?>) {
        val map: MutableMap<YearMonth, List<MonthEvent>> = mutableMapOf()
        execute(ioDispatcher) {
            months.forEach { monthData ->
                monthData?.let {
                    log("Raw month data : $monthData")
                    val data: List<MonthsDataEntity> =
                        Gson().fromJson(monthData, monthDataEntityType)
                    if (data.isNotEmpty()) {
                        listOfMonthEvents = mutableListOf()
                        listOfMonthEvents.addAll(data.map())
                    }

                    map.putAll(listOfMonthEvents.groupBy { YearMonth.from(it.startDate) } as MutableMap)
                    map.putAll(listOfMonthEvents.groupBy { YearMonth.from(it.endDate) } as MutableMap)

                    log("List Of month events : $listOfMonthEvents")
                    monthEventsMap.putAll(map)
                }
            }
        }
    }

    fun updateSelectedDayItems(events: List<Event>?, date: LocalDate?) {
        selectedDayItems.clear()
        selectedDayItems.addAll(events.orEmpty())
    }

    fun updateMonthlyItemsList(yearMonth: YearMonth) {
        var formatter = DateTimeFormatter.ofPattern("EEE, dd MMMM")
        monthlyItems.clear()

        monthEventsMap[yearMonth]?.forEach {
            monthlyItems.add(
                Event(
                    localDate = it.localDate, eventImp = it.eventImp,
                    eventName = "${it.eventName} starts from ${it.startDate.format(formatter)} " +
                            "and ends on ${it.endDate.format(formatter)}",
                    color = it.color
                )
            )
        }

        formatter = DateTimeFormatter.ofPattern("E, MMM dd")
        dayEventsPerMonthMap[yearMonth]?.forEach {
            if (it.eventImp == Importance.med || it.eventImp == Importance.high) {
                monthlyItems.add(
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