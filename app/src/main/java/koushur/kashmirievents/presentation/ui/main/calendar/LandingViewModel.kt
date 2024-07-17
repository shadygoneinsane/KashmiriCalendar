package koushur.kashmirievents.presentation.ui.main.calendar

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import koushir.kashmirievents.BR
import koushir.kashmirievents.R
import koushur.kashmirievents.database.data.DayEvent
import koushur.kashmirievents.database.data.Event
import koushur.kashmirievents.database.data.MonthEvent
import koushur.kashmirievents.database.entity.DayDataEntity
import koushur.kashmirievents.database.entity.Days
import koushur.kashmirievents.database.entity.Months
import koushur.kashmirievents.database.entity.MonthsDataEntity
import koushur.kashmirievents.database.entity.SavedEventEntity
import koushur.kashmirievents.database.entity.map
import koushur.kashmirievents.database.entity.matchDayNameFromConstants
import koushur.kashmirievents.di.module.DispatcherProvider
import koushur.kashmirievents.presentation.base.BaseViewModel
import koushur.kashmirievents.presentation.navigation.SingleLiveEvent
import koushur.kashmirievents.presentation.ui.main.calendar.uidata.UIDateEvent
import koushur.kashmirievents.presentation.ui.main.calendar.uidata.UIDateRangeEvent
import koushur.kashmirievents.repository.CalendarRepository
import koushur.kashmirievents.utility.Constants
import koushur.kashmirievents.utility.Importance
import koushur.kashmirievents.utility.fromJsonToList
import koushur.kashmirievents.utility.getDayEventImportance
import koushur.kashmirievents.utility.getYearMonth
import me.tatarka.bindingcollectionadapter2.itembindings.OnItemBindClass
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

class LandingViewModel(
    private val repository: CalendarRepository, private val dispatcher: DispatcherProvider
) : BaseViewModel() {
    private val monthNameLiveData = MutableLiveData<String>()
    private val gson = Gson()
    private var selectedYrMonth = YearMonth.now()

    /**
     * List of month events in [MonthEvent] format.
     * Example :
     * { MonthEvent(indexOfMonth=23, startDate=2025-02-28, endDate=2025-03-14, monthName=Phalgun Shukla Paksha, imp=0),
     *   MonthEvent(indexOfMonth=-1, startDate=2025-03-26, endDate=2025-03-30, monthName=Panchak, imp=2),
     *   MonthEvent(indexOfMonth=0, startDate=2025-03-15, endDate=2025-03-29, monthName=Chaitra Krishna Paksha, imp=0), ...}
     */
    private var listOfMonthEvents = mutableListOf<MonthEvent>()

    /**
     * Store list of all events month [YearMonth] wise, in [Event] format.
     * Used in [updateMonthlyItemsList] to populate list of all daily items for each month/current month on UI
     * Example:
     * {"2022-05" ->  { DayEvent(indexOfDay=1, date=2022-05-01, dayName=Okdoh, imp=0),
     *                  DayEvent(indexOfDay=2, date=2022-05-02, dayName=Dwitya, imp=0),
     *                  DayEvent(indexOfDay=3, date=2022-05-03, dayName=Tritya, imp=0),
     *                  DayEvent(indexOfDay=3, date=2022-05-04, dayName=Tritya, imp=0),.... }
     */
    private var groupedByMonthMap: Map<YearMonth, List<Event>> = emptyMap()

    /**
     * Holds a Maps [YearMonth] to a list of month events
     * Used to populate list of items [MonthEvent] for current month on UI
     * Example :
     * { "2025-03" -> { MonthEvent(indexOfMonth=23, startDate=2025-02-28, endDate=2025-03-14, monthName=Phalgun Shukla Paksha, imp=0),
     *                  MonthEvent(indexOfMonth=-1, startDate=2025-02-26, endDate=2025-03-03, monthName=Panchak, imp=2),
     *                  MonthEvent(indexOfMonth=0, startDate=2025-03-15, endDate=2025-03-29, monthName=Chaitra Krishna Paksha, imp=0)},
     *   "2025-04" -> { MonthEvent(indexOfMonth=-1, startDate=2025-04-13, endDate=2025-04-27, monthName=Vaishakh Krishna Paksha , imp=0) ,
     *                  MonthEvent(indexOfMonth=1, startDate=2025-03-29, endDate=2025-04-12, monthName=Chaitra Shukla Paksha, imp=0) }, ... }
     */
    private val monthEventsMap: MutableMap<YearMonth, MutableList<MonthEvent>> = mutableMapOf()

    /**
     * Holds a map of events grouped by local date in [LocalDate] format.
     * Used by calendar to populate list of selected day items [DayEvent] on UI
     */
    private var groupedByLocalDateMap = MutableLiveData<Map<LocalDate, List<DayEvent>>>(emptyMap())

    private val prevMonthEvent = SingleLiveEvent<Unit?>()
    private val nextMonthEvent = SingleLiveEvent<Unit?>()

    val monthlyItems = ObservableArrayList<Any>()
    val monthlyItemBinding: OnItemBindClass<Any> = createMonthlyItemBinding()


    val selectedDayItems = ObservableArrayList<Any>()
    val selectedDayItemBinding: OnItemBindClass<Any> = createSelectedDayItemBinding()

    /**
     * Processes the JSON data of events and populates the internal data structures.
     * Called only once
     */
    fun processDataFromJson(dailyEvents: List<String?>, monthlyEvents: List<String?>) {
        viewModelScope.launch(dispatcher.io()) {
            listOfMonthEvents.clear()
            monthEventsMap.clear()
            groupedByLocalDateMap.postValue(emptyMap())
            groupedByMonthMap = emptyMap()
            setMonthEventsData(monthlyEvents)
            setDaysEventsData(dailyEvents)
        }
    }

    /**
     * Deserializes month event data from JSON and populates [listOfMonthEvents].
     *
     * It then updates the [monthEventsMap] to group events by YearMonth.
     */
    private fun setMonthEventsData(allMonthsSpecialEvents: List<String?>) {
        allMonthsSpecialEvents.forEach { monthData ->
            monthData?.let {
                listOfMonthEvents.addAll(
                    gson.fromJsonToList<MonthsDataEntity>(monthData).map()
                )
            }
        }
        reUploadMonthlyEvents(listOfMonthEvents)
    }

    private fun reUploadMonthlyEvents(monthEvents: MutableList<MonthEvent>) {
        monthEventsMap.clear()
        monthEvents.forEach { monthEvent ->
            val months = setOf(
                YearMonth.from(monthEvent.startDate), YearMonth.from(monthEvent.endDate)
            )
            months.forEach { yearMonth ->
                monthEventsMap.getOrPut(yearMonth) { mutableListOf() }.add(monthEvent)
            }
        }
    }

    /**
     * Deserializes day event data from JSON and populates listDaysEvent.
     * It also groups these events by YearMonth and LocalDate.
     */
    private fun setDaysEventsData(allDaysEvents: List<String?>) {
        val listOfAllDaysEvents = mutableListOf<DayEvent>()
        allDaysEvents.forEach { allDayData ->
            allDayData?.let {
                listOfAllDaysEvents.addAll(
                    gson.fromJsonToList<DayDataEntity>(allDayData).map()
                )
            }
        }

        groupedByMonthMap = listOfAllDaysEvents.groupBy { it.localDate.getYearMonth() }
        groupedByLocalDateMap.postValue(listOfAllDaysEvents.groupBy { it.localDate })
    }

    /**
     * Populates the monthlyItems list with events for the specified [YearMonth].
     */
    fun updateMonthlyItemsList(yearMonth: YearMonth) {
        selectedYrMonth = yearMonth
        var formatter = DateTimeFormatter.ofPattern("EEE, dd MMMM")
        monthlyItems.clear()

        monthEventsMap[yearMonth]?.forEach {
            monthlyItems.add(
                UIDateRangeEvent(
                    startDate = it.localDate,
                    eventImportance = it.eventImp,
                    eventTitle = it.eventName,
                    eventStart = it.startDate.format(formatter),
                    eventEnd = it.endDate.format(formatter)
                )
            )
        }

        monthlyItems.sortBy { (it as Event).localDate }

        formatter = DateTimeFormatter.ofPattern("E, MMM dd")
        groupedByMonthMap[yearMonth]?.forEach {
            if (getDayEventImportance(it) in listOf(Importance.med, Importance.high)) {
                monthlyItems.add(
                    UIDateEvent(
                        startDate = it.localDate,
                        eventImportance = it.eventImp,
                        eventDate = it.localDate.format(formatter),
                        eventTitle = it.eventName
                    )
                )
            }
        }
    }

    /**
     * Updates the list of selected day items for display.
     */
    fun updateSelectedDayItems(events: List<DayEvent>?) {
        selectedDayItems.run {
            clear()
            addAll(events.orEmpty())
        }
    }

    /**
     * Fetches saved events from the [CalendarRepository] and updates the [monthEventsMap] with any changes.
     */
    fun updateSavedEvent() {
        viewModelScope.launch(dispatcher.io()) {
            repository.fetchAllEvents().collect { dbEvents ->
                reUploadMonthlyEvents(listOfMonthEvents)
                dbEvents.forEach { event ->
                    val monthEventsList: List<MonthEvent> =
                        listOfMonthEvents.filter { it.indexOfMonth == event.monthIndex }
                    println("Filtered months are : $monthEventsList")
                    monthEventsList.forEach { month ->
                        findEventWithinRange(event, month.startDate, month.endDate).forEach {
                            val monthEntry = MonthEvent(
                                event.monthIndex, it.date, it.date, event.eventName, Importance.high
                            )
                            val yearMonth = YearMonth.from(monthEntry.localDate)
                            monthEventsMap.getOrPut(yearMonth) { mutableListOf() }.add(monthEntry)
                        }
                    }
                }
                withContext(dispatcher.main()) {
                    updateMonthlyItemsList(selectedYrMonth)
                }
            }
        }
    }


    fun findYearMonthDetails(events: List<DayEvent>?, date: LocalDate): Bundle? {
        val dayIndex = events?.find { it.indexOfDay != -1 }?.indexOfDay ?: -1
        val dayName = Days.daysList[dayIndex]
        val monthMap: MonthEvent? =
            listOfMonthEvents.find { isDateWithinMonth(date, it.startDate, it.endDate) }
        if (monthMap == null || monthMap.indexOfMonth == -1 || (monthMap.indexOfMonth != Months.monthsMap[monthMap.monthName])) {
            return null
        }
        return bundleOf(
            Constants.EXTRA_DATE to date,
            Constants.EXTRA_MONTH_INDEX to monthMap.indexOfMonth,
            Constants.EXTRA_MONTH_NAME to monthMap.monthName,
            Constants.EXTRA_DAY_INDEX to dayIndex,
            Constants.EXTRA_DAY_NAME to dayName
        )
    }

    private fun isDateWithinMonth(
        date: LocalDate, startDate: LocalDate, endDate: LocalDate
    ) = date in startDate..endDate

    private fun findEventWithinRange(
        event: SavedEventEntity, from: LocalDate, to: LocalDate
    ): MutableList<DayEvent> {
        val dayEventsIdentified: MutableList<DayEvent> = mutableListOf()
        var date = from
        while (date.isBefore(to)) {
            groupedByLocalDateMap.value?.get(date)?.forEach { dayData: DayEvent ->
                println("For date $date, Looking for saved event : ${event.eventName}, \nThe dayData is $dayData")
                if (dayData.indexOfDay == event.dayIndex && dayData.dayName.matchDayNameFromConstants() == event.dayName) {
                    dayEventsIdentified.add(dayData)
                }
            }
            date = date.plusDays(1)
        }
        return dayEventsIdentified
    }

    private fun createMonthlyItemBinding() = OnItemBindClass<Any>().map(
        UIDateRangeEvent::class.java, BR.event, R.layout.layout_range_event_item
    ).map(UIDateEvent::class.java, BR.event, R.layout.layout_date_event_item)
        .map(Event::class.java, BR.event, R.layout.layout_special_event_item)

    private fun createSelectedDayItemBinding() = OnItemBindClass<Any>().map(
        DayEvent::class.java, BR.event, R.layout.layout_special_event_item
    ).map(Event::class.java, BR.event, R.layout.layout_special_event_item)

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