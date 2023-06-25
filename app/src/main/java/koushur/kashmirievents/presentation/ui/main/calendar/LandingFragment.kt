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
import koushur.kashmirievents.database.data.Event
import koushur.kashmirievents.presentation.base.BaseFragment
import koushur.kashmirievents.presentation.base.BaseViewModel
import koushur.kashmirievents.utility.AppConstants
import koushur.kashmirievents.utility.daysOfWeek
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
        loadDayEventsData()
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

        viewModel.getDayEventsLiveData().observe(viewLifecycleOwner) {
            //When we have complete
            val mapDateEvents = viewModel.setMonthWiseDayEventData(it)
            loadMonthDataFromAssets()
            setUpCalendar(mapDateEvents)
        }

        //setup calendar
        viewBinding.cvMain.setup(
            YearMonth.of(2022, Month.MARCH),
            YearMonth.of(2024, Month.MARCH),
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

    private fun loadDayEventsData() {
        activity?.let {
            val dailyEvents = mutableListOf<String?>()
            dailyEvents.add(loadJSONFromAsset(it, AppConstants.dbDayEvents_22_23))
            dailyEvents.add(loadJSONFromAsset(it, AppConstants.dbDayEvents_23_24))
            viewModel.fetchDaysEventsData(dailyEvents)
        }
    }

    private fun loadMonthDataFromAssets() {
        activity?.let {
            val monthlyEvents = mutableListOf<String?>()
            monthlyEvents.add(loadJSONFromAsset(it, AppConstants.dbMonthEvents_23_24))

            monthlyEvents.add(loadJSONFromAsset(it, AppConstants.dbMonthEvents_22_23))
            viewModel.fetchMonthsEventsData(monthlyEvents)
        }
    }

    private fun setUpCalendar(mapDateEvents: Map<LocalDate, List<DayEvent>>) {
        viewBinding.cvMain.dayBinder = CVDayBinder(mapDateEvents)
        viewBinding.cvMain.monthHeaderBinder = CVMonthHeaderBinder()

        //to update list with current months items as default when calendar is setup
        updateData(YearMonth.now())

        viewBinding.cvMain.monthScrollListener = { month ->
            updateData(month.yearMonth)

            selectedDate?.let {
                // Clear selection if we scroll to a new month.
                selectedDate = null
                viewBinding.cvMain.notifyDateChanged(it)
                viewModel.updateSelectedDayItems(null, null)
            }
        }

        viewBinding.cvMain.scrollToMonth(YearMonth.now())
    }

    private fun updateData(yearMonth: YearMonth) {
        viewModel.updateMonthlyItemsList(yearMonth)
        viewModel.setMonthName(yearMonth)
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

    inner class CVDayBinder(private val mapDateEvents: Map<LocalDate, List<Event>>) :
        MonthDayBinder<DayViewContainer> {
        override fun create(view: View) = DayViewContainer(view, mapDateEvents)
        override fun bind(container: DayViewContainer, data: CalendarDay) {
            container.day = data
            container.dateTV.text = data.date.dayOfMonth.toString()

            container.topView.background = null
            container.bottomView.background = null

            if (data.position == DayPosition.MonthDate) {
                setDateDataAndColor(selectedDate, mapDateEvents[data.date], container, data)
            } else {
                container.dateTV.setTextColorRes(R.color.cv_text_grey_light)
                container.layout.background = null
            }
        }
    }

    inner class DayViewContainer(
        view: View, private val mapDateEvents: Map<LocalDate, List<Event>>
    ) : ViewContainer(view) {
        lateinit var day: CalendarDay // Will be set when this container is bound.
        private val binding = CalendarDayBinding.bind(view)
        val dateTV: TextView = binding.dateText
        val layout = binding.exFiveDayLayout
        val topView: View = binding.dayTop
        val topTV: TextView = binding.dayTopText
        val bottomView: View = binding.dayBottom
        val bottomTV: TextView = binding.dayBottomText

        init {
            view.setOnClickListener { selectDate(day.date, mapDateEvents) }
        }

        private fun selectDate(date: LocalDate, mapDateEvents: Map<LocalDate, List<Event>>) {
            if (selectedDate != date) {
                val oldDate = selectedDate
                selectedDate = date
                viewBinding.cvMain.notifyDateChanged(date)
                oldDate?.let { viewBinding.cvMain.notifyDateChanged(it) }
                viewModel.updateSelectedDayItems(mapDateEvents[date], date)
            }
        }
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