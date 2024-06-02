package koushur.kashmirievents.presentation.ui.main.addevent

import android.os.Bundle
import android.view.View
import koushir.kashmirievents.R
import koushir.kashmirievents.databinding.FragmentAddEventBinding
import koushur.kashmirievents.presentation.base.BaseFragment
import koushur.kashmirievents.presentation.base.BaseViewModel
import koushur.kashmirievents.presentation.navigation.Navigator
import koushur.kashmirievents.utility.Constants
import koushur.kashmirievents.utility.toast
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * A [BaseFragment] for showing featured items in a list
 *
 * Author: Vikesh Dass
 * Created on: 29-10-2020
 * Email : vikeshdass@gmail.com
 */
class AddEventFragment : BaseFragment<FragmentAddEventBinding>(R.layout.fragment_add_event) {

    private val viewModel: AddEventViewModel by viewModel()

    override fun provideViewModel(): BaseViewModel = viewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.viewModel = viewModel

        viewModel.getSaveEvent().observe(viewLifecycleOwner) {
            "Saved".toast(requireContext())
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
            val formatter = DateTimeFormatter.ofPattern("EEE, dd MMMM")
            if (localDate != null) {
                viewBinding.tvDate.text = "Date: ${localDate.format(formatter)}" +
                        "\nMonth: $monthName" +
                        "\nDay: $dayName"

                viewBinding.saveButton.setOnClickListener {
                    val eventName = viewBinding.eventNameEdittext.editText?.text?.toString() ?: ""
                    if (eventName.isEmpty()) return@setOnClickListener
                    else viewModel.saveEvent(
                        eventName, localDate, monthIndex, monthName, dayIndex, dayName
                    )
                }
            }
        }
    }

    companion object {
        fun newInstance(
            args: Bundle
        ): AddEventFragment {
            val fragment = AddEventFragment()
            fragment.arguments = args
            return fragment
        }
    }
}