package koushur.kashmirievents.presentation.ui.main.calendar

import android.app.Activity
import android.graphics.Typeface
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.core.view.children
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import com.kizitonwose.calendarview.utils.next
import com.kizitonwose.calendarview.utils.previous
import kotlinx.android.synthetic.main.layout_cv_day_legend.view.*
import koushir.kashmirievents.BR
import koushir.kashmirievents.R
import koushir.kashmirievents.databinding.CalendarDayBinding
import koushir.kashmirievents.databinding.FragmentLandingBinding
import koushur.kashmirievents.data.Event
import koushur.kashmirievents.data.Importance
import koushur.kashmirievents.presentation.base.BaseFragment
import koushur.kashmirievents.presentation.base.BaseViewModel
import koushur.kashmirievents.presentation.utils.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.io.IOException
import java.io.InputStream
import java.time.LocalDate
import java.time.Month
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.*

class LandingFragment : BaseFragment<FragmentLandingBinding>() {
    override fun layoutId(): Int = R.layout.fragment_landing
    private val viewModel: LandingViewModel by viewModel()
    private var selectedDate: LocalDate? = null

    override fun provideViewModel(): BaseViewModel? {
        return viewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.prevMonthEvent.observe(this, {
            viewBinding.cvMain.findFirstVisibleMonth()?.let {
                viewBinding.cvMain.smoothScrollToMonth(it.yearMonth.previous)
            }
        })

        viewModel.nextMonthEvent.observe(this, {
            viewBinding.cvMain.findFirstVisibleMonth()?.let {
                viewBinding.cvMain.smoothScrollToMonth(it.yearMonth.next)
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.setVariable(BR.viewModel, viewModel)
        viewBinding.executePendingBindings()

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

        activity?.let {
            loadJSONFromAsset(it, AppConstants.dbEvents)?.let { jsonStr ->
                viewModel.fetchEventsData(jsonStr)
            }

            loadJSONFromAsset(it, AppConstants.dbSpecialEvents)?.let { jsonStr ->
                viewModel.fetchSpecialEventsData(jsonStr)
            }
        }

        val daysOfWeek = daysOfWeekFromLocale()
        val currentMonth = YearMonth.now()
        viewBinding.cvMain.setup(
            YearMonth.of(2020, Month.MARCH),
            YearMonth.of(2021, Month.APRIL),
            daysOfWeek.first()
        )
        viewBinding.cvMain.scrollToMonth(currentMonth)
        viewModel.updateSpecialItemsList(currentMonth)

        viewBinding.cvMain.dayBinder = object : DayBinder<DayViewContainer> {
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

        viewBinding.cvMain.monthHeaderBinder =
            object : MonthHeaderFooterBinder<MonthViewContainer> {
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

        viewBinding.cvMain.monthScrollListener = { month ->
            viewModel.setMonthName(month)

            viewModel.updateSpecialItemsList(month.yearMonth)

            selectedDate?.let {
                // Clear selection if we scroll to a new month.
                selectedDate = null
                viewBinding.cvMain.notifyDateChanged(it)
                viewModel.updateList(null)
            }
        }
    }

    inner class DayViewContainer(view: View) : ViewContainer(view) {
        lateinit var day: CalendarDay // Will be set when this container is bound.
        private val binding = CalendarDayBinding.bind(view)
        val dateTV: TextView = binding.dateText
        val layout = binding.exFiveDayLayout
        val topView: View = binding.dayTop
        val topTV: TextView = binding.dayTopText
        val bottomView: View = binding.dayBottom
        val bottomTV: TextView = binding.dayBottomText

        init {
            view.setOnClickListener { selectDate(day.date) }
        }
    }

    private fun setDatDataAndColor(container: DayViewContainer, day: CalendarDay) {
        container.dateTV.setTextColorRes(R.color.cv_text_grey)
        //highlighting today's date
        container.layout.setBackgroundResource(
            if (day.date != viewModel.getToday()) {
                if (selectedDate == day.date) R.drawable.drawable_selected_bg
                else 0
            } else
                R.drawable.drawable_selected_highlighted_bg
        )

        viewModel.getEvents()?.let { map ->
            val eventsList = map[day.date]
            eventsList?.let { listEvents ->
                when {
                    listEvents.count() == 1 -> {
                        //Timber.d("Date : ${day.date} | Event found is ${listEvents[0]}")

                        setTextViewData(container.topTV, container.topView, eventsList[0])

                        container.bottomTV.text = ""
                        container.bottomTV.makeGone()
                        container.bottomView.makeGone()
                    }
                    listEvents.count() == 2 -> {
                        container.dateTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 8f)

                        //Timber.d("Date : ${day.date} | Events found are 1: ${listEvents[0]} 2: ${listEvents[1]}")
                        setTextViewData(container.topTV, container.topView, eventsList[0])
                        setTextViewData(container.bottomTV, container.bottomView, eventsList[1])
                    }
                    else -> {
                        //Timber.d("Date : ${day.date} | Events found $listEvents}")
                    }
                }
            }
        }
    }

    private fun setTextViewData(tv: TextView, decorView: View, event: Event) {
        tv.text = event.eventName
        decorView.setBackgroundColor(tv.context.getColorCompat(event.color))

        tv.makeVisible()
        decorView.makeVisible()

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
                tv.setTextColor(tv.context.getColorCompat(R.color.white))
            }
        }
    }

    private fun selectDate(date: LocalDate) {
        if (selectedDate != date) {
            val oldDate = selectedDate
            selectedDate = date
            viewBinding.cvMain.notifyDateChanged(date)
            oldDate?.let { viewBinding.cvMain.notifyDateChanged(it) }
            viewModel.updateList(date)
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