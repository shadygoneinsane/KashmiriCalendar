package koushur.kashmirievents.presentation.ui.main

import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.children
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import com.kizitonwose.calendarview.utils.next
import com.kizitonwose.calendarview.utils.previous
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.example_5_calendar_day.view.*
import kotlinx.android.synthetic.main.fragment_landing.*
import kotlinx.android.synthetic.main.layout_cv_month_header.view.*
import kotlinx.android.synthetic.main.layout_event_item.view.*
import koushir.kashmirievents.BR
import koushir.kashmirievents.R
import koushir.kashmirievents.databinding.FragmentLandingBinding
import koushur.kashmirievents.data.Event
import koushur.kashmirievents.presentation.base.BaseFragment
import koushur.kashmirievents.presentation.utils.daysOfWeekFromLocale
import koushur.kashmirievents.presentation.utils.getColorCompat
import koushur.kashmirievents.presentation.utils.inflate
import koushur.kashmirievents.presentation.utils.setTextColorRes
import org.threeten.bp.LocalDate
import org.threeten.bp.YearMonth
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.TextStyle
import java.util.*

class EventsAdapter : RecyclerView.Adapter<EventsAdapter.Example5FlightsViewHolder>() {
    val events = mutableListOf<Event>()
    private val formatter = DateTimeFormatter.ofPattern("EEE' 'dd MMM'\n'HH:mm")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Example5FlightsViewHolder {
        return Example5FlightsViewHolder(parent.inflate(R.layout.layout_event_item))
    }

    override fun onBindViewHolder(viewHolder: Example5FlightsViewHolder, position: Int) {
        viewHolder.bind(events[position])
    }

    override fun getItemCount(): Int = events.size

    inner class Example5FlightsViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bind(event: Event) {
            containerView.eventDate.text = formatter.format(event.time)
            containerView.eventDate.setBackgroundColor(itemView.context.getColorCompat(event.color))

            containerView.eventName.text = event.name
        }
    }
}

class LandingFragment : BaseFragment<FragmentLandingBinding, LandingViewModel>() {

    override fun provideViewModelClass() = LandingViewModel::class.java
    override fun layoutId(): Int = R.layout.fragment_landing
    override val viewModelVariable = BR.viewModel
    private val today = LocalDate.now()
    private val monthTitleFormatter = DateTimeFormatter.ofPattern("MMMM")
    private val eventsAdapter = EventsAdapter()
    private val events = generateEvents().groupBy { it.time.toLocalDate() }
    private var selectedDate: LocalDate? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        exFiveRv.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        exFiveRv.adapter = eventsAdapter
        exFiveRv.addItemDecoration(DividerItemDecoration(requireContext(), RecyclerView.VERTICAL))
        eventsAdapter.notifyDataSetChanged()

        val daysOfWeek = daysOfWeekFromLocale()
        val currentMonth = YearMonth.now()
        exFiveCalendar.setup(
            currentMonth.minusMonths(10),
            currentMonth.plusMonths(10),
            daysOfWeek.first()
        )
        exFiveCalendar.scrollToMonth(currentMonth)

        class DayViewContainer(view: View) : ViewContainer(view) {
            lateinit var day: CalendarDay // Will be set when this container is bound.
            val textView = view.exFiveDayText
            val layout = view.exFiveDayLayout
            val flightTopView = view.exFiveDayFlightTop
            val flightBottomView = view.exFiveDayFlightBottom

            init {
                view.setOnClickListener {
                    if (day.owner == DayOwner.THIS_MONTH) {
                        if (selectedDate != day.date) {
                            val oldDate = selectedDate
                            selectedDate = day.date
                            exFiveCalendar.notifyDateChanged(day.date)
                            oldDate?.let { exFiveCalendar.notifyDateChanged(it) }
                            updateAdapterForDate(day.date)
                        }
                    }
                }
            }
        }

        exFiveCalendar.dayBinder = object : DayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)
            override fun bind(container: DayViewContainer, day: CalendarDay) {
                container.day = day
                val textView = container.textView
                val layout = container.layout
                textView.text = day.date.dayOfMonth.toString()

                val flightTopView = container.flightTopView
                val flightBottomView = container.flightBottomView

                flightTopView.background = null
                flightBottomView.background = null

                if (day.owner == DayOwner.THIS_MONTH) {
                    textView.setTextColorRes(R.color.example_5_text_grey)
                    layout.setBackgroundResource(if (selectedDate == day.date) R.drawable.example_5_selected_bg else 0)

                    val flights = events[day.date]
                    if (flights != null) {
                        if (flights.count() == 1) {
                            flightBottomView.setBackgroundColor(view.context.getColorCompat(flights[0].color))
                        } else {
                            flightTopView.setBackgroundColor(view.context.getColorCompat(flights[0].color))
                            flightBottomView.setBackgroundColor(view.context.getColorCompat(flights[1].color))
                        }
                    }
                } else {
                    textView.setTextColorRes(R.color.example_5_text_grey_light)
                    layout.background = null
                }
            }
        }

        class MonthViewContainer(view: View) : ViewContainer(view) {
            val legendLayout = view.legendLayout
        }
        exFiveCalendar.monthHeaderBinder = object : MonthHeaderFooterBinder<MonthViewContainer> {
            override fun create(view: View) = MonthViewContainer(view)
            override fun bind(container: MonthViewContainer, month: CalendarMonth) {
                // Setup each header day text if we have not done that already.
                if (container.legendLayout.tag == null) {
                    container.legendLayout.tag = month.yearMonth
                    container.legendLayout.children.map { it as TextView }
                        .forEachIndexed { index, tv ->
                            tv.text =
                                daysOfWeek[index].getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
                                    .toUpperCase(Locale.ENGLISH)
                            tv.setTextColorRes(R.color.example_5_text_grey)
                            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
                        }
                    month.yearMonth
                }
            }
        }

        exFiveCalendar.monthScrollListener = { month ->
            val title = "${monthTitleFormatter.format(month.yearMonth)} ${month.yearMonth.year}"
            exFiveMonthYearText.text = title

            selectedDate?.let {
                // Clear selection if we scroll to a new month.
                selectedDate = null
                exFiveCalendar.notifyDateChanged(it)
                updateAdapterForDate(null)
            }
        }

        exFiveNextMonthImage.setOnClickListener {
            exFiveCalendar.findFirstVisibleMonth()?.let {
                exFiveCalendar.smoothScrollToMonth(it.yearMonth.next)
            }
        }

        exFivePreviousMonthImage.setOnClickListener {
            exFiveCalendar.findFirstVisibleMonth()?.let {
                exFiveCalendar.smoothScrollToMonth(it.yearMonth.previous)
            }
        }
    }

    fun generateEvents(): List<Event> {
        val list = mutableListOf<Event>()
        val currentMonth = YearMonth.now()

        val currentMonth17 = currentMonth.atDay(17)
        list.add(Event(currentMonth17.atTime(14, 0), "Ashtami", R.color.brown_700))
        list.add(Event(currentMonth17.atTime(14, 0), "Pancham", R.color.blue_800))

        val currentMonth22 = currentMonth.atDay(22)
        list.add(Event(currentMonth22.atTime(14, 0), "Ashtami", R.color.blue_800))
        list.add(Event(currentMonth22.atTime(14, 0), "Pancham", R.color.brown_700))

        return list
    }

    override fun onStart() {
        super.onStart()
        requireActivity().window.statusBarColor =
            requireContext().getColorCompat(R.color.example_5_toolbar_color)
    }

    override fun onStop() {
        super.onStop()
        requireActivity().window.statusBarColor =
            requireContext().getColorCompat(R.color.colorPrimaryDark)
    }


    private fun updateAdapterForDate(date: LocalDate?) {
        eventsAdapter.events.clear()
        eventsAdapter.events.addAll(events[date].orEmpty())
        eventsAdapter.notifyDataSetChanged()
    }
}