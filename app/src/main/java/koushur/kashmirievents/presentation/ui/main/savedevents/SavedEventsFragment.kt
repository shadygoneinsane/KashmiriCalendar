package koushur.kashmirievents.presentation.ui.main.savedevents

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import koushir.kashmirievents.R
import koushir.kashmirievents.databinding.FragmentSavedEventsBinding
import koushur.kashmirievents.presentation.base.BaseFragment
import koushur.kashmirievents.presentation.base.BaseViewModel
import koushur.kashmirievents.presentation.navigation.Navigator
import koushur.kashmirievents.presentation.ui.main.calendar.ItemDivider
import koushur.kashmirievents.utility.showMaterialAlert
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
            context.showMaterialAlert(
                title = getString(R.string.label_title_warn_delete),
                message = getString(R.string.label_message_warn_delete, it.eventName),
                positive = getString(R.string.label_confirm),
                negative = getString(R.string.label_cancel)
            ) { dialog, which ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> {
                        viewModel.deleteEvent(it)
                    }

                    DialogInterface.BUTTON_NEGATIVE -> {
                        dialog.dismiss()
                    }
                }
            }
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

        viewModel.getDeletedEvent().observe(viewLifecycleOwner) {
            getString(R.string.deleted_successfully).toast(requireActivity())
        }
    }
}