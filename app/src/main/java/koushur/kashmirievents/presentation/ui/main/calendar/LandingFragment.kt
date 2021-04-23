package koushur.kashmirievents.presentation.ui.main.calendar

import android.app.Activity
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.children
import androidx.lifecycle.lifecycleScope
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import com.kizitonwose.calendarview.utils.next
import com.kizitonwose.calendarview.utils.previous
import kotlinx.android.synthetic.main.layout_cv_day_legend.view.*
import kotlinx.coroutines.launch
import koushir.kashmirievents.BR
import koushir.kashmirievents.R
import koushir.kashmirievents.databinding.CalendarDayBinding
import koushir.kashmirievents.databinding.FragmentLandingBinding
import koushur.kashmirievents.data.Event
import koushur.kashmirievents.presentation.base.BaseFragment
import koushur.kashmirievents.presentation.base.BaseViewModel
import koushur.kashmirievents.presentation.utils.AppConstants
import koushur.kashmirievents.presentation.utils.daysOfWeekFromLocale
import koushur.kashmirievents.presentation.utils.setDateDataAndColor
import koushur.kashmirievents.presentation.utils.setTextColorRes
import org.koin.android.viewmodel.ext.android.viewModel
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

        viewModel.getPrevMonthClickEvent().observe(this, {
            viewBinding.cvMain.findFirstVisibleMonth()?.let {
                viewBinding.cvMain.smoothScrollToMonth(it.yearMonth.previous)
            }
        })

        viewModel.getNextMonthClickEvent().observe(this, {
            viewBinding.cvMain.findFirstVisibleMonth()?.let {
                viewBinding.cvMain.smoothScrollToMonth(it.yearMonth.next)
            }
        })

        viewModel.getEventsLiveData().observe(this, {
            val mapDateEvents = viewModel.setEventsData(it)

            setUpCalendar(mapDateEvents)
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.setVariable(BR.viewModel, viewModel)
        viewBinding.executePendingBindings()

        //setup calendar
        viewBinding.cvMain.setup(
            YearMonth.of(2020, Month.MARCH),
            YearMonth.of(2022, Month.APRIL),
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
                loadJSONFromAsset(it, AppConstants.dbEvents_20_21)?.let { allEvents ->
                    viewModel.fetchEventsData(allEvents, viewModel.getEventsLiveData())
                }

                loadJSONFromAsset(it, AppConstants.dbEvents_21_22)?.let { allEvents ->
                    viewModel.fetchEventsData(allEvents, viewModel.getEventsLiveData())
                }

                loadJSONFromAsset(it, AppConstants.dbSpecialEvents_20_21)?.let { specialEvents ->
                    viewModel.fetchSpecialEventsData(specialEvents)
                }

                loadJSONFromAsset(it, AppConstants.dbSpecialEvents_21_22)?.let { specialEvents ->
                    viewModel.fetchSpecialEventsData(specialEvents)
                }
            }
        }
    }

    private fun setUpCalendar(mapDateEvents: Map<LocalDate, List<Event>>) {
        viewBinding.cvMain.dayBinder = CVDayBinder(mapDateEvents)
        viewBinding.cvMain.monthHeaderBinder = CVMonthHeaderBinder()

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

    inner class CVMonthHeaderBinder : MonthHeaderFooterBinder<MonthViewContainer> {
        override fun create(view: View) = MonthViewContainer(view)
        override fun bind(container: MonthViewContainer, month: CalendarMonth) {
            // Setup each header day text if we have not done that already.
            if (container.legendLayout.tag == null) {
                container.legendLayout.tag = month.yearMonth
                container.legendLayout.children.map { it as TextView }
                    .forEachIndexed { index, tv ->
                        tv.text = daysOfWeek[index]
                            .getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
                            .toUpperCase(Locale.ENGLISH)
                        tv.setTextColorRes(R.color.cv_text_grey)
                        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
                    }
            }
        }
    }

    inner class MonthViewContainer(view: View) : ViewContainer(view) {
        val legendLayout: LinearLayout = view.legendLayout
    }

    inner class CVDayBinder(private val mapDateEvents: Map<LocalDate, List<Event>>) :
        DayBinder<DayViewContainer> {
        override fun create(view: View) = DayViewContainer(view, mapDateEvents)
        override fun bind(container: DayViewContainer, day: CalendarDay) {
            container.day = day
            container.dateTV.text = day.date.dayOfMonth.toString()

            container.topView.background = null
            container.bottomView.background = null

            if (day.owner == DayOwner.THIS_MONTH) {
                setDateDataAndColor(selectedDate, mapDateEvents[day.date], container, day)
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