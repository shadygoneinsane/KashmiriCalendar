package koushur.kashmirievents.presentation.ui.main

import android.app.Activity
import android.graphics.Typeface
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.children
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import com.kizitonwose.calendarview.utils.next
import com.kizitonwose.calendarview.utils.previous
import kotlinx.android.synthetic.main.calendar_day.view.*
import kotlinx.android.synthetic.main.fragment_landing.*
import kotlinx.android.synthetic.main.layout_cv_month_header.view.*
import koushir.kashmirievents.BR
import koushir.kashmirievents.R
import koushir.kashmirievents.databinding.FragmentLandingBinding
import koushur.kashmirievents.data.Event
import koushur.kashmirievents.data.Importance
import koushur.kashmirievents.database.entity.MonthDataEntity
import koushur.kashmirievents.presentation.base.BaseFragment
import koushur.kashmirievents.presentation.utils.daysOfWeekFromLocale
import koushur.kashmirievents.presentation.utils.getColorCompat
import koushur.kashmirievents.presentation.utils.setTextColorRes
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.Month
import org.threeten.bp.YearMonth
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.TextStyle
import java.io.IOException
import java.io.InputStream
import java.util.*

class LandingFragment : BaseFragment<FragmentLandingBinding, LandingViewModel>() {
    override fun provideViewModelClass() = LandingViewModel::class.java
    override fun layoutId(): Int = R.layout.fragment_landing
    override val viewModelVariable = BR.viewModel

    private lateinit var events: Map<LocalDate, List<Event>>
    private var selectedDate: LocalDate? = null

    private val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.prevMonthEvent.observe(this, Observer {
            exFiveCalendar.findFirstVisibleMonth()?.let {
                exFiveCalendar.smoothScrollToMonth(it.yearMonth.previous)
            }
        })

        viewModel.nextMonthEvent.observe(this, Observer {
            exFiveCalendar.findFirstVisibleMonth()?.let {
                exFiveCalendar.smoothScrollToMonth(it.yearMonth.next)
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        exFiveRv.addItemDecoration(DividerItemDecoration(requireContext(), RecyclerView.VERTICAL))

        activity?.let {
            loadJSONFromAsset(it)?.let { jsonStr ->
                viewModel.fetchData(jsonStr)
            }
        }

        viewModel.data.observe(this, Observer { dataList ->
            if (dataList.isNotEmpty()) {
                events = generateEvents(dataList).groupBy { it.time.toLocalDate() }
            } else Toast.makeText(requireContext(), "empty data ", Toast.LENGTH_LONG).show()
        })

        val daysOfWeek = daysOfWeekFromLocale()
        val currentMonth = YearMonth.now()
        exFiveCalendar.setup(
            YearMonth.of(2020, Month.MARCH),
            YearMonth.of(2021, Month.MARCH),
            daysOfWeek.first()
        )
        exFiveCalendar.scrollToMonth(currentMonth)

        exFiveCalendar.dayBinder = object : DayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)
            override fun bind(container: DayViewContainer, day: CalendarDay) {
                container.day = day
                container.dateTV.text = day.date.dayOfMonth.toString()

                val bottomDecorView = container.bottomView

                container.topView.background = null
                bottomDecorView.background = null

                if (day.owner == DayOwner.THIS_MONTH) {
                    setDatDataAndColor(container, day)
                } else {
                    container.dateTV.setTextColorRes(R.color.cv_text_grey_light)
                    container.layout.background = null
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
                            tv.setTextColorRes(R.color.cv_text_grey)
                            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
                        }
                    month.yearMonth
                }
            }
        }

        exFiveCalendar.monthScrollListener = { month ->
            viewModel.setMonthName(month)

            selectedDate?.let {
                // Clear selection if we scroll to a new month.
                selectedDate = null
                exFiveCalendar.notifyDateChanged(it)
                updateAdapterForDate(null)
            }
        }
    }

    private fun generateEvents(inputData: List<MonthDataEntity>): List<Event> {
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

    inner class DayViewContainer(view: View) : ViewContainer(view) {
        lateinit var day: CalendarDay // Will be set when this container is bound.
        val dateTV: TextView = view.dateText
        val layout = view.exFiveDayLayout
        val topView: View = view.dayTop
        val topTV: TextView = view.dayTopText
        val bottomView: View = view.dayBottom
        val bottomTV: TextView = view.dayBottomText

        init {
            view.setOnClickListener {
                if (day.owner == DayOwner.THIS_MONTH && selectedDate != day.date) {
                    val oldDate = selectedDate
                    selectedDate = day.date
                    exFiveCalendar.notifyDateChanged(day.date)
                    oldDate?.let { exFiveCalendar.notifyDateChanged(it) }
                    updateAdapterForDate(day.date)
                }
            }
        }
    }

    private fun setDatDataAndColor(container: DayViewContainer, day: CalendarDay) {
        container.dateTV.setTextColorRes(R.color.cv_text_grey)
        container.layout.setBackgroundResource(if (selectedDate == day.date) R.drawable.drawable_selected_bg else 0)

        val eventsList = events[day.date]
        eventsList?.let {
            if (it.count() == 1) {
                setTextViewData(container.topTV, container.topView, eventsList[0])

                container.bottomTV.text = ""
                container.bottomTV.visibility = View.GONE
                container.bottomView.visibility = View.GONE
            } else {
                setTextViewData(container.topTV, container.topView, eventsList[0])

                setTextViewData(container.bottomTV, container.bottomView, eventsList[1])
            }
        }
    }

    private fun setTextViewData(tv: TextView, decorView: View, event: Event) {
        tv.text = event.eventName
        decorView.setBackgroundColor(tv.context.getColorCompat(event.color))

        when (event.eventImp) {
            Importance.high -> {
                tv.setTypeface(Typeface.DEFAULT, Typeface.BOLD)
                tv.setTextColor(tv.context.getColorCompat(R.color.red_800))
            }
            Importance.med -> {
                tv.setTypeface(Typeface.DEFAULT, Typeface.BOLD)
                tv.setTextColor(tv.context.getColorCompat(R.color.white))
            }
            Importance.low -> {
                tv.setTypeface(Typeface.DEFAULT, Typeface.NORMAL)
                tv.setTextColor(tv.context.getColorCompat(R.color.white))

            }
            else -> {
                tv.setTypeface(Typeface.DEFAULT, Typeface.NORMAL)
                tv.setTextColor(tv.context.getColorCompat(R.color.white))
            }
        }
    }

    override fun onStart() {
        super.onStart()
        requireActivity().window.statusBarColor =
            requireContext().getColorCompat(R.color.cv_toolbar_color)
    }

    override fun onStop() {
        super.onStop()
        requireActivity().window.statusBarColor =
            requireContext().getColorCompat(R.color.colorPrimaryDark)
    }


    private fun updateAdapterForDate(date: LocalDate?) {
        viewModel.updateList(events[date].orEmpty())
    }

    private fun loadJSONFromAsset(activity: Activity): String? {
        return try {
            val input: InputStream = activity.assets.open("database/20_21.json")
            val size: Int = input.available()
            val buffer = ByteArray(size)
            input.read(buffer)
            input.close()
            String(buffer, Charsets.UTF_8)
        } catch (ex: IOException) {
            ex.printStackTrace()
            null
        }
    }
}