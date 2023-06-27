package koushur.kashmirievents.presentation.ui.main.calendar

import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import koushir.kashmirievents.BR
import koushir.kashmirievents.R
import koushur.kashmirievents.database.data.DayEvent
import koushur.kashmirievents.database.data.Event
import koushur.kashmirievents.database.data.MonthEvent
import koushur.kashmirievents.database.entity.DayDataEntity
import koushur.kashmirievents.database.entity.MonthsDataEntity
import koushur.kashmirievents.database.entity.map
import koushur.kashmirievents.di.module.DispatcherProvider
import koushur.kashmirievents.presentation.base.BaseViewModel
import koushur.kashmirievents.presentation.navigation.SingleLiveEvent
import koushur.kashmirievents.repository.CalendarRepository
import koushur.kashmirievents.utility.Importance
import koushur.kashmirievents.utility.getDayEventImportance
import koushur.kashmirievents.utility.getYearMonth
import koushur.kashmirievents.utility.log
import me.tatarka.bindingcollectionadapter2.itembindings.OnItemBindClass
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

class LandingViewModel(
    private val repository: CalendarRepository,
    private val dispatcher: DispatcherProvider
) : BaseViewModel() {
    private val listDayDataEntityType = object : TypeToken<List<DayDataEntity>>() {}.type
    private val monthNameLiveData = MutableLiveData<String>()
    private val monthDataEntityType = object : TypeToken<List<MonthsDataEntity>>() {}.type
    private var listOfMonthEvents = mutableListOf<MonthEvent>()

    /**
     * Used to store list of all events day wise, in [Event] format
     */
    private val listDaysEvent = mutableListOf<DayEvent>()

    private var groupedByMonthMap: Map<YearMonth, List<Event>> = emptyMap()
    private var groupedByLocalDateMap = MutableLiveData<Map<LocalDate, List<DayEvent>>>(emptyMap())

    /**
     * Used to populate list of items for current month on UI
     */
    private val monthEventsMap: MutableMap<YearMonth, List<MonthEvent>> = mutableMapOf()

    private val prevMonthEvent = SingleLiveEvent<Unit?>()
    private val nextMonthEvent = SingleLiveEvent<Unit?>()

    val monthlyItems = ObservableArrayList<Any>()
    val monthlyItemBinding: OnItemBindClass<Any> =
        OnItemBindClass<Any>().map(Event::class.java, BR.event, R.layout.layout_special_event_item)

    val selectedDayItems = ObservableArrayList<Any>()
    val selectedDayItemBinding: OnItemBindClass<Any> = OnItemBindClass<Any>().map(
        DayEvent::class.java,
        BR.event,
        R.layout.layout_special_event_item
    ).map(Event::class.java, BR.event, R.layout.layout_special_event_item)


    fun processEventsDataFromJson(
        allDaysEvents: List<String?>, allMonthsSpecialEvents: List<String?>
    ) {
        execute(dispatcher.io()) {
            setMonthEventsData(allMonthsSpecialEvents)
            setDaysEventsData(allDaysEvents)
        }
    }

    private fun setDaysEventsData(allDaysEvents: List<String?>) {
        val listOfAllDaysEvents = mutableListOf<DayEvent>()
        allDaysEvents.forEach { allDayData ->
            allDayData?.let {
                val data: List<DayDataEntity> = Gson().fromJson(allDayData, listDayDataEntityType)
                listOfAllDaysEvents.addAll(data.map())
            }
        }
        listDaysEvent.addAll(listOfAllDaysEvents)

        val groupedByYearMonth = listOfAllDaysEvents.groupBy { it.localDate.getYearMonth() }
        val groupedByLocalDate = listOfAllDaysEvents.groupBy { it.localDate }

        groupedByMonthMap = groupedByYearMonth
        groupedByLocalDateMap.postValue(groupedByLocalDate)
    }

    private fun setMonthEventsData(allMonthsSpecialEvents: List<String?>) {
        val mapOfSpecialMonthEvents: MutableMap<YearMonth, List<MonthEvent>> = mutableMapOf()
        allMonthsSpecialEvents.forEach { monthData ->
            monthData?.let {
                val data: List<MonthsDataEntity> = Gson().fromJson(monthData, monthDataEntityType)
                if (data.isNotEmpty()) {
                    listOfMonthEvents =
                        listOfMonthEvents.ifEmpty { mutableListOf() }
                    listOfMonthEvents.addAll(data.map())
                }

                mapOfSpecialMonthEvents.putAll(listOfMonthEvents.groupBy { YearMonth.from(it.startDate) } as MutableMap)
                mapOfSpecialMonthEvents.putAll(listOfMonthEvents.groupBy { YearMonth.from(it.endDate) } as MutableMap)
            }
        }

        log("List of month events is :: $listOfMonthEvents")
        monthEventsMap.putAll(mapOfSpecialMonthEvents)
    }

    fun updateSelectedDayItems(events: List<DayEvent>?, date: LocalDate?) {
        selectedDayItems.clear()
        selectedDayItems.addAll(events.orEmpty())
    }

    fun updateMonthlyItemsList(yearMonth: YearMonth, placeholderString: String) {
        var formatter = DateTimeFormatter.ofPattern("EEE, dd MMMM")
        monthlyItems.clear()

        monthEventsMap[yearMonth]?.forEach {
            monthlyItems.add(
                Event(
                    localDate = it.localDate, eventImp = it.eventImp, eventName = String.format(
                        placeholderString,
                        it.eventName,
                        it.startDate.format(formatter),
                        it.endDate.format(formatter)
                    )
                )
            )
        }

        formatter = DateTimeFormatter.ofPattern("E, MMM dd")
        groupedByMonthMap[yearMonth]?.forEach {
            val eventImportance = getDayEventImportance(it)
            if (eventImportance == Importance.med || eventImportance == Importance.high) {
                monthlyItems.add(
                    Event(
                        localDate = it.localDate,
                        eventImp = it.eventImp,
                        eventName = "On ${it.localDate.format(formatter)} - ${it.eventName} "
                    )
                )
            }
        }
    }

    fun getMonthName() = monthNameLiveData
    fun getDayEventsLiveData() = groupedByLocalDateMap
    fun getNextMonthClickEvent() = nextMonthEvent
    fun getPrevMonthClickEvent() = prevMonthEvent

    fun setMonthName(yearMonth: YearMonth) {
        val monthTitleFormatter = DateTimeFormatter.ofPattern("MMMM")
        monthNameLiveData.value = "${monthTitleFormatter.format(yearMonth)} ${yearMonth.year}"
    }

    fun leftClickEvent() {
        prevMonthEvent.call()
    }

    fun rightClickEvent() {
        nextMonthEvent.call()
    }
}