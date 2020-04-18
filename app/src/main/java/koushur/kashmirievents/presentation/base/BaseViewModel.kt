package koushur.kashmirievents.presentation.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import koushur.kashmirievents.presentation.common.SingleEvent
import koushur.kashmirievents.presentation.navigation.SingleLiveEvent

open class BaseViewModel : ViewModel() {

    /**
     * Live data to handle error
     */
    private var errorLiveData = MediatorLiveData<SingleEvent<String>>()

    val mErrorLiveData: LiveData<SingleEvent<String>>
        get() = errorLiveData

    /**
     * Live data to handle loading
     */
    private var loadingLiveData = SingleLiveEvent<Boolean>()

    val mLoadingLiveData: SingleLiveEvent<Boolean>
        get() = loadingLiveData


    protected fun handleError(error: String) {
        errorLiveData.postValue(SingleEvent(error))
    }
}