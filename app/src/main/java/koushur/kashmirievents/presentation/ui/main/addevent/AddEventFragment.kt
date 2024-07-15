package koushur.kashmirievents.presentation.ui.main.addevent

import android.os.Bundle
import android.view.View
import koushir.kashmirievents.R
import koushir.kashmirievents.databinding.FragmentAddEventBinding
import koushur.kashmirievents.database.entity.SavedEventEntity
import koushur.kashmirievents.presentation.base.BaseFragment
import koushur.kashmirievents.presentation.base.BaseViewModel
import koushur.kashmirievents.presentation.navigation.Navigator
import koushur.kashmirievents.utility.Constants
import koushur.kashmirievents.utility.DateUtils
import koushur.kashmirievents.utility.toast
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * A [BaseFragment] for storing users events/festivals
 *
 * Author: Vikesh Dass
 * Created on: 03-06-2024
 * Email : vikeshdass@gmail.com
 */
class AddEventFragment : BaseFragment<FragmentAddEventBinding>(R.layout.fragment_add_event) {

    private val viewModel: AddEventViewModel by viewModel()

    override fun provideViewModel(): BaseViewModel = viewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.viewModel = viewModel

        viewModel.getSaveEvent().observe(viewLifecycleOwner) {
            Navigator.navigateToPreviousFrag(requireActivity())
        }

        viewModel.getErrorEvent().observe(viewLifecycleOwner) {
            getString(R.string.something_wrong).toast(requireContext())
        }

        viewModel.getCancelEvent().observe(viewLifecycleOwner) {
            Navigator.navigateToPreviousFrag(requireActivity())
        }

        arguments?.let { args ->
            val monthIndex: Int = args.getInt(Constants.EXTRA_MONTH_INDEX)
            val monthName: String? = args.getString(Constants.EXTRA_MONTH_NAME)
            val dayIndex: Int = args.getInt(Constants.EXTRA_DAY_INDEX)
            val dayName: String? = args.getString(Constants.EXTRA_DAY_NAME)
            val localDate: LocalDate? = args.get(Constants.EXTRA_DATE) as LocalDate?
            val formatter = DateTimeFormatter.ofPattern(DateUtils.dayMonth)
            if (localDate != null) {
                viewBinding.tvSelectedDate.text = localDate.format(formatter)
                viewBinding.tvTithi.text = dayName
                viewBinding.tvPaksha.text = monthName

                viewBinding.saveButton.setOnClickListener {
                    val eventName = viewBinding.eventNameEdittext.editText?.text?.toString() ?: ""
                    if (eventName.isNotEmpty() && monthName != null && dayName != null) {
                        val event = SavedEventEntity(
                            selectedDate = localDate,
                            monthIndex = monthIndex,
                            monthName = monthName,
                            dayIndex = dayIndex,
                            dayName = dayName,
                            eventName = eventName
                        )
                        viewModel.saveEvent(event)
                    } else {
                        return@setOnClickListener
                    }
                }
            }
        }
    }
}