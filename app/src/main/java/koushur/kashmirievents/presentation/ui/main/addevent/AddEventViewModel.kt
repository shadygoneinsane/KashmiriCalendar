package koushur.kashmirievents.presentation.ui.main.addevent

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import koushur.kashmirievents.database.entity.SavedEventEntity
import koushur.kashmirievents.di.module.DispatcherProvider
import koushur.kashmirievents.presentation.base.BaseViewModel
import koushur.kashmirievents.presentation.navigation.SingleLiveEvent
import koushur.kashmirievents.repository.CalendarRepository

class AddEventViewModel(
    private val repository: CalendarRepository, private val dispatcher: DispatcherProvider
) : BaseViewModel() {
    private val cancelListener = SingleLiveEvent<Unit?>()
    private val saveClickListener = SingleLiveEvent<Unit?>()

    fun cancelEvent() = cancelListener.call()

    fun getCancelEvent() = cancelListener

    fun saveEvent(event: SavedEventEntity) {
        viewModelScope.launch(dispatcher.io()) {
            repository.saveEvent(event)?.let {
                withContext(dispatcher.main()) {
                    saveClickListener.call()
                }
            } ?: run {
                withContext(dispatcher.main()) {
                    errorListener.call()
                }
            }
        }
    }

    fun getSaveEvent() = saveClickListener
}