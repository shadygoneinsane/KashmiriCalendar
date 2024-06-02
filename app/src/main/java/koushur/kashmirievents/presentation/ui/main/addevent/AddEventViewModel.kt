package koushur.kashmirievents.presentation.ui.main.addevent

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import koushur.kashmirievents.database.entity.SavedEventEntity
import koushur.kashmirievents.di.module.DispatcherProvider
import koushur.kashmirievents.presentation.base.BaseViewModel
import koushur.kashmirievents.presentation.navigation.SingleLiveEvent
import koushur.kashmirievents.repository.CalendarRepository
import java.time.LocalDate

class AddEventViewModel(
    private val repository: CalendarRepository, private val dispatcher: DispatcherProvider
) : BaseViewModel() {
    private val cancelListener = SingleLiveEvent<Unit?>()
    private val saveClickListener = SingleLiveEvent<Unit?>()
    private val errorListener = SingleLiveEvent<Unit?>()

    fun cancelEvent() = cancelListener.call()

    fun getCancelEvent() = cancelListener

    fun saveEvent(
        eventName: String,
        date: LocalDate,
        monthIndex: Int,
        monthName: String?,
        dayIndex: Int,
        dayName: String?
    ) {
        if (eventName.isNotEmpty() && monthName != null && dayName != null) {
            viewModelScope.launch(dispatcher.io()) {
                repository.saveEvent(
                    SavedEventEntity(
                        selectedDate = date,
                        monthIndex = monthIndex,
                        monthName = monthName,
                        dayIndex = dayIndex,
                        dayName = dayName,
                        eventName = eventName
                    )
                )?.let {
                    viewModelScope.launch(dispatcher.main()) {
                        saveClickListener.call()
                    }
                } ?: run {
                    viewModelScope.launch(dispatcher.main()) {
                        errorListener.call()
                    }
                }
            }
        } else {
            errorListener.call()
        }
    }

    fun getSaveEvent() = saveClickListener

    fun getErrorEvent() = errorListener
}