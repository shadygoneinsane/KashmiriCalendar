package koushur.kashmirievents.presentation.ui.main.calendar

import android.os.Bundle
import androidx.core.os.bundleOf
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
import koushur.kashmirievents.database.entity.Days
import koushur.kashmirievents.database.entity.MonthsDataEntity
import koushur.kashmirievents.database.entity.map
import koushur.kashmirievents.di.module.DispatcherProvider
import koushur.kashmirievents.presentation.base.BaseViewModel
import koushur.kashmirievents.presentation.navigation.SingleLiveEvent
import koushur.kashmirievents.repository.CalendarRepository
import koushur.kashmirievents.utility.Constants
import koushur.kashmirievents.utility.Importance
import koushur.kashmirievents.utility.getDayEventImportance
import koushur.kashmirievents.utility.getYearMonth
import me.tatarka.bindingcollectionadapter2.itembindings.OnItemBindClass
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

class LandingViewModel(
    private val repository: CalendarRepository, private val dispatcher: DispatcherProvider
) : BaseViewModel() {
    private val listDayDataEntityType = object : TypeToken<List<DayDataEntity>>() {}.type
    private val monthNameLiveData = MutableLiveData<String>()
    private val monthDataEntityType = object : TypeToken<List<MonthsDataEntity>>() {}.type
    private var listOfMonthEvents = mutableListOf<MonthEvent>()
    private val gson = Gson()

    /**
     * Used to store list of all events day wise, in [Event] format
     */
    private val listDaysEvent = mutableListOf<DayEvent>()

    private var groupedByMonthMap: Map<YearMonth, List<Event>> = emptyMap()
    private var groupedByLocalDateMap = MutableLiveData<Map<LocalDate, List<DayEvent>>>(emptyMap())

    /**
     * Used to populate list of items for current month on UI
     */
    private val monthEventsMap: MutableMap<YearMonth, MutableList<MonthEvent>> = mutableMapOf()

    private val prevMonthEvent = SingleLiveEvent<Unit?>()
    private val nextMonthEvent = SingleLiveEvent<Unit?>()

    val monthlyItems = ObservableArrayList<Any>()
    val monthlyItemBinding: OnItemBindClass<Any> =
        OnItemBindClass<Any>().map(Event::class.java, BR.event, R.layout.layout_special_event_item)

    val selectedDayItems = ObservableArrayList<Any>()
    val selectedDayItemBinding: OnItemBindClass<Any> = OnItemBindClass<Any>().map(
        DayEvent::class.java, BR.event, R.layout.layout_special_event_item
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
                val data: List<DayDataEntity> = gson.fromJson(allDayData, listDayDataEntityType)
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
        allMonthsSpecialEvents.forEach { eachSpecialMonthData ->
            eachSpecialMonthData?.let {
                val data: List<MonthsDataEntity> =
                    gson.fromJson(eachSpecialMonthData, monthDataEntityType)
                if (data.isNotEmpty()) {
                    listOfMonthEvents.addAll(data.map())
                }
            }
        }
        listOfMonthEvents.forEach { monthEvent ->
            val months = mutableSetOf(
                YearMonth.from(monthEvent.startDate), YearMonth.from(monthEvent.endDate)
            )
            months.forEach { yearMonth ->
                monthEventsMap.getOrPut(yearMonth) { mutableListOf() }.add(monthEvent)
            }
        }
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

        monthlyItems.sortBy { (it as Event).localDate }

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

    fun findYearMonthDetails(events: List<DayEvent>?, date: LocalDate): Bundle {
        val dayIndex = events?.find { it.indexOfDay != -1 }?.indexOfDay ?: -1
        val dayName = Days.daysList[dayIndex]
        val monthMap: MonthEvent? =
            listOfMonthEvents.find { isDateWithinMonth(date, it.startDate, it.endDate) }
        val monthIndex = monthMap?.indexOfMonth ?: -1
        val monthName = monthMap?.monthName ?: "Not found"
        return bundleOf(
            Constants.EXTRA_DATE to date,
            Constants.EXTRA_MONTH_INDEX to monthIndex,
            Constants.EXTRA_MONTH_NAME to monthName,
            Constants.EXTRA_DAY_INDEX to dayIndex,
            Constants.EXTRA_DAY_NAME to dayName
        )
    }

    private fun isDateWithinMonth(
        date: LocalDate, startDate: LocalDate, endDate: LocalDate
    ) = date in startDate..endDate

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
