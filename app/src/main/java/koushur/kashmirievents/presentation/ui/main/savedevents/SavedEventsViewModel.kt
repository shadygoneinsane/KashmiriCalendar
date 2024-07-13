package koushur.kashmirievents.presentation.ui.main.savedevents

import androidx.databinding.ObservableArrayList
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import koushir.kashmirievents.BR
import koushir.kashmirievents.R
import koushur.kashmirievents.database.entity.SavedEventEntity
import koushur.kashmirievents.di.module.DispatcherProvider
import koushur.kashmirievents.interfaces.OnOptionClickListener
import koushur.kashmirievents.presentation.base.BaseViewModel
import koushur.kashmirievents.presentation.navigation.SingleLiveEvent
import koushur.kashmirievents.repository.CalendarRepository
import me.tatarka.bindingcollectionadapter2.itembindings.OnItemBindClass

class SavedEventsViewModel(
    private val repository: CalendarRepository, private val dispatcher: DispatcherProvider
) : BaseViewModel() {
    private val cancelListener = SingleLiveEvent<Unit?>()

    private val itemDeletedListener = SingleLiveEvent<Unit?>()
    private val deleteItemClick = SingleLiveEvent<SavedEventEntity>()

    private val itemUpdatedItemClick = SingleLiveEvent<Unit?>()
    private val updateItemClick = SingleLiveEvent<SavedEventEntity>()

    private val saveClickListener = SingleLiveEvent<Unit?>()
    private val errorListener = SingleLiveEvent<Unit?>()

    val savedEvents = ObservableArrayList<Any>()
    val savedEventsBinding: OnItemBindClass<Any> =
        OnItemBindClass<Any>().map(SavedEventEntity::class.java) { itemBinding, _, item ->
            itemBinding.clearExtras().set(BR.event, R.layout.layout_item_saved_event)
                .bindExtra(BR.clickListener, object : OnOptionClickListener<Int> {
                    override fun onOptionClick(option: Int) {
                        if (option == 1) {
                            updateItemClick.value = item
                        } else {
                            deleteItemClick.value = item
                        }
                    }
                })
        }

    fun cancelEvent() = cancelListener.call()

    fun getCancelEvent() = cancelListener

    fun saveEvent(event: SavedEventEntity) {
        viewModelScope.launch(dispatcher.io()) {
            repository.saveEvent(event)?.let {
                viewModelScope.launch(dispatcher.main()) {
                    saveClickListener.call()
                }
            } ?: run {
                viewModelScope.launch(dispatcher.main()) {
                    errorListener.call()
                }
            }
        }
    }

    fun fetchAllEvents() {
        viewModelScope.launch(dispatcher.io()) {
            repository.fetchAllEvents().collect { savedItems ->
                viewModelScope.launch(dispatcher.main()) {
                    savedEvents.clear()
                    savedEvents.addAll(savedItems)
                }
            }
        }
    }

    fun deleteEvent(event: SavedEventEntity) {
        viewModelScope.launch(dispatcher.io()) {
            repository.deleteEvent(event).let {
                viewModelScope.launch(dispatcher.main()) {
                    itemDeletedListener.call()
                }
            }
        }
    }

    fun getSaveEvent() = saveClickListener

    fun getErrorEvent() = errorListener

    fun getDeleteItemClickedEvent() = deleteItemClick

    fun getUpdateItemClickedEvent() = updateItemClick

    fun getDeletedEvent() = itemDeletedListener
}