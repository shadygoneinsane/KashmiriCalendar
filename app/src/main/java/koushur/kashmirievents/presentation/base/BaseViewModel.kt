package koushur.kashmirievents.presentation.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

open class BaseViewModel : ViewModel() {

    fun execute(context: CoroutineContext, block: () -> Unit) {
        viewModelScope.launch {
            withContext(context) {
                block.invoke()
            }
        }
    }
}