package koushur.kashmirievents.presentation.ui.main.savedevents

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import koushir.kashmirievents.R
import koushir.kashmirievents.databinding.FragmentSavedEventsBinding
import koushur.kashmirievents.database.entity.SavedEventEntity
import koushur.kashmirievents.presentation.base.BaseFragment
import koushur.kashmirievents.presentation.base.BaseViewModel
import koushur.kashmirievents.presentation.navigation.Navigator
import koushur.kashmirievents.presentation.ui.main.calendar.ItemDivider
import koushur.kashmirievents.utility.setTextColorRes
import koushur.kashmirievents.utility.toast
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * A [BaseFragment] for storing users events/festivals
 *
 * Author: Vikesh Dass
 * Created on: 13-07-2024
 * Email : vikeshdass@gmail.com
 */
class SavedEventsFragment :
    BaseFragment<FragmentSavedEventsBinding>(R.layout.fragment_saved_events) {

    private val viewModel: SavedEventsViewModel by viewModel()

    override fun provideViewModel(): BaseViewModel = viewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.viewModel = viewModel
        viewModel.fetchAllEvents()
        viewModel.getSaveEvent().observe(viewLifecycleOwner) {
            Navigator.navigateToPreviousFrag(requireActivity())
        }

        viewModel.getErrorEvent().observe(viewLifecycleOwner) {
            getString(R.string.something_wrong).toast(requireContext())
        }

        viewModel.getDeleteItemClickedEvent().observe(viewLifecycleOwner) {
            showDialog(it)
        }

        viewModel.getUpdateItemClickedEvent().observe(viewLifecycleOwner) {
            "Feature coming soon".toast(requireActivity())
        }

        viewModel.getCancelEvent().observe(viewLifecycleOwner) {
            Navigator.navigateToPreviousFrag(requireActivity())
        }

        viewBinding.listSavedEvents.addItemDecoration(
            ItemDivider(
                resources.getDimensionPixelSize(R.dimen.dimen_4dp),
                resources.getDimensionPixelSize((R.dimen.dimen_4dp))
            )
        )
    }

    private fun showDialog(savedEventEntity: SavedEventEntity) {
        val dialogClickListener = DialogInterface.OnClickListener { dialog, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> {
                    viewModel.deleteEvent(savedEventEntity)
                }

                DialogInterface.BUTTON_NEGATIVE -> {
                    dialog.dismiss()
                }
            }
        }

        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setMessage(getString(R.string.label_title_warn_delete, savedEventEntity.eventName))
            .setPositiveButton(getString(R.string.label_yes), dialogClickListener)
            .setNegativeButton(getString(R.string.label_no), dialogClickListener)
        val alert = builder.create()
        alert.setOnShowListener {
            alert.getButton(AlertDialog.BUTTON_POSITIVE).apply {
                setTextColorRes(R.color.snow)
                isAllCaps = false
            }
            alert.getButton(AlertDialog.BUTTON_NEGATIVE).apply {
                setTextColorRes(R.color.snow)
                isAllCaps = false
            }
        }
        alert.show()
    }
}