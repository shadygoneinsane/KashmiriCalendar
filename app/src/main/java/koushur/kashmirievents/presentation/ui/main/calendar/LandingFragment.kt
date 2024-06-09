package koushur.kashmirievents.presentation.ui.main.calendar

import android.app.Activity
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.children
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.nextMonth
import com.kizitonwose.calendar.core.previousMonth
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.MonthHeaderFooterBinder
import com.kizitonwose.calendar.view.ViewContainer
import koushir.kashmirievents.BR
import koushir.kashmirievents.R
import koushir.kashmirievents.databinding.CalendarDayBinding
import koushir.kashmirievents.databinding.CalendarHeaderBinding
import koushir.kashmirievents.databinding.FragmentLandingBinding
import koushur.kashmirievents.database.data.DayEvent
import koushur.kashmirievents.presentation.base.BaseFragment
import koushur.kashmirievents.presentation.base.BaseViewModel
import koushur.kashmirievents.utility.AppConstants
import koushur.kashmirievents.utility.daysOfWeek
import koushur.kashmirievents.utility.makeGone
import koushur.kashmirievents.utility.setDateDataAndColor
import koushur.kashmirievents.utility.setTextColorRes
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.IOException
import java.io.InputStream
import java.time.LocalDate
import java.time.Month
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

class LandingFragment : BaseFragment<FragmentLandingBinding>(R.layout.fragment_landing) {
    private val viewModel: LandingViewModel by viewModel()
    private var selectedDate: LocalDate? = null
    val daysOfWeek = daysOfWeek()

    override fun provideViewModel(): BaseViewModel {
        return viewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //fetch data from assets
        loadEveryAvailableDayEventsData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.setVariable(BR.viewModel, viewModel)
        viewBinding.executePendingBindings()

        viewModel.getPrevMonthClickEvent().observe(viewLifecycleOwner) {
            viewBinding.cvMain.findFirstVisibleMonth()?.let {
                viewBinding.cvMain.smoothScrollToMonth(it.yearMonth.previousMonth)
            }
        }

        viewModel.getNextMonthClickEvent().observe(viewLifecycleOwner) {
            viewBinding.cvMain.findFirstVisibleMonth()?.let {
                viewBinding.cvMain.smoothScrollToMonth(it.yearMonth.nextMonth)
            }
        }

        viewModel.getDayEventsLiveData().observe(viewLifecycleOwner) { mapDateEvents ->
            setUpCalendar(mapDateEvents)
        }

        //setup calendar
        viewBinding.cvMain.setup(
            YearMonth.of(2022, Month.MARCH),
            YearMonth.of(2025, Month.APRIL),
            daysOfWeek.first()
        )

        viewBinding.rvHighlightEvents.addItemDecoration(
            ItemDivider(
                resources.getDimensionPixelSize(R.dimen.dimen_4dp),
                resources.getDimensionPixelSize((R.dimen.dimen_4dp))
            )
        )

        viewBinding.rvMajorEvents.addItemDecoration(
            ItemDivider(
                resources.getDimensionPixelSize(R.dimen.dimen_4dp),
                resources.getDimensionPixelSize((R.dimen.dimen_4dp))
            )
        )
    }

    private fun loadEveryAvailableDayEventsData() {
        activity?.let {
            val dailyEvents = mutableListOf<String?>().apply {
                add(loadJSONFromAsset(it, AppConstants.dbDayEvents_22_23))
                add(loadJSONFromAsset(it, AppConstants.dbDayEvents_23_24))
                add(loadJSONFromAsset(it, AppConstants.dbDayEvents_24_25))
            }

            val monthlyEvents = mutableListOf<String?>().apply {
                add(loadJSONFromAsset(it, AppConstants.dbMonthEvents_23_24))
                add(loadJSONFromAsset(it, AppConstants.dbMonthEvents_22_23))
                add(loadJSONFromAsset(it, AppConstants.dbMonthEvents_24_25))
            }

            viewModel.processEventsDataFromJson(dailyEvents, monthlyEvents)
        }
    }

    private fun setUpCalendar(mapDateEvents: Map<LocalDate, List<DayEvent>>) {
        viewBinding.cvMain.dayBinder = CVDayBinder(mapDateEvents)
        viewBinding.cvMain.monthHeaderBinder = CVMonthHeaderBinder()

        viewBinding.cvMain.monthScrollListener = { month ->
            viewModel.updateMonthlyItemsList(
                month.yearMonth,
                getString(R.string.special_event_placeholder)
            )
            viewModel.setMonthName(month.yearMonth)

            selectedDate?.let {
                selectedDate = null
                viewBinding.cvMain.notifyDateChanged(it)
                viewModel.updateSelectedDayItems(null, null)
            }
        }

        viewBinding.cvMain.scrollToMonth(YearMonth.now())
    }

    inner class CVDayBinder(private val mapDateEvents: Map<LocalDate, List<DayEvent>>) :
        MonthDayBinder<DayViewContainer> {
        override fun create(view: View) = DayViewContainer(view, mapDateEvents)
        override fun bind(container: DayViewContainer, data: CalendarDay) {
            container.onBind(data)
        }
    }

    inner class DayViewContainer(
        view: View, private val mapDateEvents: Map<LocalDate, List<DayEvent>>
    ) : ViewContainer(view) {
        lateinit var date: LocalDate // Will be set when this container is bound.
        private val binding = CalendarDayBinding.bind(view)
        private val dateTV: TextView = binding.dateText
        private val layout = binding.exFiveDayLayout
        private val topView: View = binding.dayTop
        private val topTV: TextView = binding.dayTopText
        private val bottomView: View = binding.dayBottom
        private val bottomTV: TextView = binding.dayBottomText

        fun onBind(data: CalendarDay) {
            this.date = data.date
            val eventsForTheDay: List<DayEvent>? = mapDateEvents[data.date]

            view.setOnClickListener {
                selectDate(date, eventsForTheDay)
            }
            dateTV.text = data.date.dayOfMonth.toString()

            topView.background = null
            bottomView.background = null

            when (data.position) {
                DayPosition.MonthDate -> {
                    dateTV.setTextColorRes(R.color.cv_text_grey)
                    setBackgroundDateData()
                    setDateDataAndColor(
                        eventsForTheDay, topTV, topView, bottomTV, bottomView, dateTV
                    )
                }

                DayPosition.InDate, DayPosition.OutDate -> {
                    dateTV.setTextColorRes(R.color.cv_text_grey_light)
                    topTV.makeGone()
                    topView.makeGone()
                    bottomView.makeGone()
                    bottomTV.makeGone()
                    bottomView.makeGone()
                    layout.background = null
                }

                else -> {
                    dateTV.setTextColorRes(R.color.cv_text_grey_light)
                    layout.background = null
                }
            }
        }

        private fun selectDate(date: LocalDate, eventsForTheDay: List<DayEvent>?) {
            if (selectedDate != date) {
                val oldDate = selectedDate
                selectedDate = date
                viewBinding.cvMain.notifyDateChanged(date)
                oldDate?.let { viewBinding.cvMain.notifyDateChanged(it) }
                viewModel.updateSelectedDayItems(eventsForTheDay, date)
            }
        }

        private fun setBackgroundDateData() {
            layout.setBackgroundResource(
                if (date != LocalDate.now()) {
                    if (selectedDate == date) R.drawable.drawable_selected_bg
                    else 0
                } else R.drawable.drawable_selected_highlighted_bg
            )
        }
    }

    inner class CVMonthHeaderBinder : MonthHeaderFooterBinder<MonthViewContainer> {
        override fun create(view: View) = MonthViewContainer(view)
        override fun bind(container: MonthViewContainer, data: CalendarMonth) {
            // Setup each header day text if we have not done that already.
            if (container.legendLayout.tag == null) {
                container.legendLayout.tag = data.yearMonth
                container.legendLayout.children.map { it as TextView }
                    .forEachIndexed { index, tv ->
                        tv.text =
                            daysOfWeek[index].getDisplayName(TextStyle.SHORT, Locale.getDefault())
                        tv.setTextColorRes(R.color.cv_text_grey)
                        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
                    }
            }
        }
    }

    inner class MonthViewContainer(view: View) : ViewContainer(view) {
        val legendLayout: LinearLayout = CalendarHeaderBinding.bind(view).legendLayout.root
    }

    private fun loadJSONFromAsset(activity: Activity, fileName: String): String? {
        return try {
            val input: InputStream = activity.assets.open(fileName)
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
