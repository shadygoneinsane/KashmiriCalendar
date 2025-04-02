package koushur.kashmirievents.presentation.base

import androidx.lifecycle.ViewModel
import koushur.kashmirievents.presentation.navigation.SingleLiveEvent

open class BaseViewModel : ViewModel() {
    protected val errorListener = SingleLiveEvent<Unit?>()

    fun getErrorEvent() = errorListener
}