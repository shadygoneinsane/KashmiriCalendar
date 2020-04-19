package koushur.kashmirievents.presentation.ui.main

import android.view.View
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kizitonwose.calendarview.model.CalendarMonth
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.layout_event_item.view.*
import koushir.kashmirievents.BR
import koushir.kashmirievents.R
import koushur.kashmirievents.data.Event
import koushur.kashmirievents.data.Importance
import koushur.kashmirievents.database.entity.MonthDataEntity
import koushur.kashmirievents.presentation.base.BaseViewModel
import koushur.kashmirievents.presentation.navigation.SingleLiveEvent
import koushur.kashmirievents.presentation.utils.getColorCompat
import koushur.kashmirievents.repository.CalendarRepository
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter
import me.tatarka.bindingcollectionadapter2.ItemBinding
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import javax.inject.Inject

class LandingViewModel @Inject constructor(private val repository: CalendarRepository) :
    BaseViewModel() {

    val monthName = MutableLiveData<String>()
    private lateinit var events: Map<LocalDate, List<Event>>
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
            list.add(Event(todayLocalDate, it.imp, it.events, returnColor(it.imp)))
        }

        return list
    }


    private fun returnColor(@Importance imp: Int): Int {
        return when (imp) {
            Importance.high -> R.color.red_800
            Importance.med -> R.color.teal_700
            Importance.low -> R.color.brown_700
            else -> R.color.brown_700
        }
    }

    fun fetchData(jsonData: String) {
        val myType = object : TypeToken<List<MonthDataEntity>>() {}.type
        val data = Gson().fromJson<List<MonthDataEntity>>(jsonData, myType)
        if (data.isNotEmpty()) {
            events = generateEvents(data).groupBy { it.time.toLocalDate() }
        }

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

    val items = ObservableArrayList<Event>()
    fun updateList(date: LocalDate?) {
        items.clear()
        items.addAll(events[date].orEmpty())
    }

    val itemBinding = ItemBinding.of<Event>(BR.event, R.layout.layout_event_item)
    /*.bindExtra(BR.clickListener, object : OnOptionClickListener<Event> {
        override fun onOptionClick(option: Event) {

        }
    })*/

    val viewHolder =
        BindingRecyclerViewAdapter.ViewHolderFactory { binding -> EventsViewHolder(binding.root) }

    inner class EventsViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(event: Event) {
            containerView.eventName.setBackgroundColor(itemView.context.getColorCompat(event.color))
            containerView.eventName.text = event.eventName
        }
    }
}