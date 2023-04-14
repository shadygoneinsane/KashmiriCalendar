package koushur.kashmirievents.presentation.ui.main.calendar

import android.app.Activity
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.children
import androidx.lifecycle.lifecycleScope
import com.kizitonwose.calendar.core.*
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.MonthHeaderFooterBinder
import com.kizitonwose.calendar.view.ViewContainer
import kotlinx.coroutines.launch
import koushir.kashmirievents.BR
import koushir.kashmirievents.R
import koushir.kashmirievents.databinding.CalendarDayBinding
import koushir.kashmirievents.databinding.CalendarHeaderBinding
import koushir.kashmirievents.databinding.FragmentLandingBinding
import koushur.kashmirievents.data.Event
import koushur.kashmirievents.presentation.base.BaseFragment
import koushur.kashmirievents.presentation.base.BaseViewModel
import koushur.kashmirievents.presentation.utils.AppConstants
import koushur.kashmirievents.presentation.utils.daysOfWeekFromLocale
import koushur.kashmirievents.presentation.utils.setDateDataAndColor
import koushur.kashmirievents.presentation.utils.setTextColorRes
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.IOException
import java.io.InputStream
import java.time.LocalDate
import java.time.Month
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.*

class LandingFragment : BaseFragment<FragmentLandingBinding>(R.layout.fragment_landing) {
    private val viewModel: LandingViewModel by viewModel()
    private var selectedDate: LocalDate? = null
    val daysOfWeek = daysOfWeekFromLocale()

    override fun provideViewModel(): BaseViewModel {
        return viewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getPrevMonthClickEvent().observe(this) {
            viewBinding.cvMain.findFirstVisibleMonth()?.let {
                viewBinding.cvMain.smoothScrollToMonth(it.yearMonth.previousMonth)
            }
        }

        viewModel.getNextMonthClickEvent().observe(this) {
            viewBinding.cvMain.findFirstVisibleMonth()?.let {
                viewBinding.cvMain.smoothScrollToMonth(it.yearMonth.nextMonth)
            }
        }

        viewModel.getEventsLiveData().observe(this) {
            val mapDateEvents = viewModel.setEventsData(it)

            setUpCalendar(mapDateEvents)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.setVariable(BR.viewModel, viewModel)
        viewBinding.executePendingBindings()

        //setup calendar
        viewBinding.cvMain.setup(
            YearMonth.of(2022, Month.MARCH),
            YearMonth.of(2023, Month.JUNE),
            daysOfWeek.first()
        )

        //fetch data from assets
        loadDataFromAssets()

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

    private fun loadDataFromAssets() {
        activity?.let {
            lifecycleScope.launch {
                /*loadJSONFromAsset(it, AppConstants.dbEvents_20_21)?.let { allEvents ->
                    viewModel.fetchEventsData(allEvents, viewModel.getEventsLiveData())
                }

                loadJSONFromAsset(it, AppConstants.dbEvents_21_22)?.let { allEvents ->
                    viewModel.fetchEventsData(allEvents, viewModel.getEventsLiveData())
                }*/

                loadJSONFromAsset(it, AppConstants.dbEvents_22_23)?.let { allEvents ->
                    viewModel.fetchEventsData(allEvents, viewModel.getEventsLiveData())
                }

                loadJSONFromAsset(it, AppConstants.dbEvents_23_24)?.let { allEvents ->
                    viewModel.fetchEventsData(allEvents, viewModel.getEventsLiveData())
                }

                /*loadJSONFromAsset(it, AppConstants.dbSpecialEvents_20_21)?.let { specialEvents ->
                    viewModel.fetchSpecialEventsData(specialEvents)
                }

                loadJSONFromAsset(it, AppConstants.dbSpecialEvents_21_22)?.let { specialEvents ->
                    viewModel.fetchSpecialEventsData(specialEvents)
                }*/

                loadJSONFromAsset(it, AppConstants.dbSpecialEvents_22_23)?.let { specialEvents ->
                    viewModel.fetchSpecialEventsData(specialEvents)
                }

                loadJSONFromAsset(it, AppConstants.dbSpecialEvents_23_24)?.let { specialEvents ->
                    viewModel.fetchSpecialEventsData(specialEvents)
                }
            }
        }
    }

    private fun setUpCalendar(mapDateEvents: Map<LocalDate, List<Event>>) {
        viewBinding.cvMain.dayBinder = CVDayBinder(mapDateEvents)
        viewBinding.cvMain.monthHeaderBinder =
            object : MonthHeaderFooterBinder<MonthViewContainer> {
                override fun create(view: View) = MonthViewContainer(view)
                override fun bind(container: MonthViewContainer, data: CalendarMonth) {
                    // Setup each header day text if we have not done that already.
                    if (container.legendLayout.tag == null) {
                        container.legendLayout.tag = data.yearMonth
                        container.legendLayout.children.map { it as TextView }
                            .forEachIndexed { index, tv ->
                                tv.text = daysOfWeek[index]
                                    .getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
                                    .uppercase(Locale.ENGLISH)
                                tv.setTextColorRes(R.color.cv_text_grey)
                                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
                            }
                    }
                }
            }

        //to update list with current months items as default when calendar is setup
        viewModel.updateSpecialItemsList(YearMonth.now())

        viewBinding.cvMain.monthScrollListener = { month ->
            viewModel.updateSpecialItemsList(month.yearMonth)

            selectedDate?.let {
                // Clear selection if we scroll to a new month.
                selectedDate = null
                viewBinding.cvMain.notifyDateChanged(it)
                viewModel.updateSelectedDayItems(null)
            }
        }

        viewBinding.cvMain.scrollToMonth(YearMonth.now())
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
    }

    private fun selectDate(date: LocalDate, mapDateEvents: Map<LocalDate, List<Event>>) {
        if (selectedDate != date) {
            val oldDate = selectedDate
            selectedDate = date
            viewBinding.cvMain.notifyDateChanged(date)
            oldDate?.let { viewBinding.cvMain.notifyDateChanged(it) }
            viewModel.updateSelectedDayItems(mapDateEvents[date])
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