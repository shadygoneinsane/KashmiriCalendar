package koushur.kashmirievents.presentation.ui.main.addevent

import android.app.Activity
import android.app.AlarmManager
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.core.content.ContextCompat
import koushir.kashmirievents.R
import koushir.kashmirievents.databinding.FragmentAddEventBinding
import koushur.kashmirievents.database.entity.SavedEventEntity
import koushur.kashmirievents.presentation.base.BaseFragment
import koushur.kashmirievents.presentation.base.BaseViewModel
import koushur.kashmirievents.presentation.navigation.Navigator
import koushur.kashmirievents.utility.Constants
import koushur.kashmirievents.utility.DateUtils
import koushur.kashmirievents.utility.showMaterialAlert
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

                setSaveClick(monthIndex, monthName, dayIndex, dayName, localDate)
            }
        }
    }

    private fun setSaveClick(
        monthIndex: Int, monthName: String?, dayIndex: Int, dayName: String?, localDate: LocalDate
    ) {
        viewBinding.saveButton.setOnClickListener {
            val eventName = viewBinding.eventNameEdittext.editText?.text?.toString() ?: ""
            if (eventName.isEmpty()) {
                context?.showMaterialAlert(
                    title = getString(R.string.label_error),
                    message = getString(R.string.name_cant_be_empty),
                    neutral = getString(R.string.label_ok)
                )
                return@setOnClickListener
            }
            if (monthName != null && dayName != null) {
                if (!checkIfUserCanSetAlarms(requireActivity())) {
                    context?.showMaterialAlert(
                        message = getString(R.string.message_alarm_permission_needed),
                        positive = getString(R.string.label_ok)
                    ) { dialog, which ->
                        when (which) {
                            DialogInterface.BUTTON_POSITIVE -> {
                                openScheduleExactAlarmsSettings()
                            }

                            DialogInterface.BUTTON_NEGATIVE -> {
                                dialog.dismiss()
                            }
                        }
                    }
                } else {
                    saveUserEvent(monthIndex, monthName, dayIndex, dayName, localDate, eventName)
                }
            } else {
                complain(getString(R.string.something_wrong))
            }
        }
    }

    private fun saveUserEvent(
        monthIndex: Int,
        monthName: String,
        dayIndex: Int,
        dayName: String,
        localDate: LocalDate,
        eventName: String
    ) {
        val event = SavedEventEntity(
            selectedDate = localDate,
            monthIndex = monthIndex,
            monthName = monthName,
            dayIndex = dayIndex,
            dayName = dayName,
            eventName = eventName
        )
        viewModel.saveEvent(event)

    }

    private fun openScheduleExactAlarmsSettings(): Boolean {
        var canScheduleAlarms = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            activity?.let { activity ->
                canScheduleAlarms = false
                Intent().also { intent ->
                    intent.action = Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
                    activity.startActivity(intent)
                }
            }
        }

        return canScheduleAlarms
    }

    private fun checkIfUserCanSetAlarms(activity: Activity): Boolean {
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmManager = ContextCompat.getSystemService(
                activity, AlarmManager::class.java
            )
            return alarmManager?.canScheduleExactAlarms() == true
        }*/
        return true
    }
}