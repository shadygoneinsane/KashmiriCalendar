package koushur.kashmirievents.presentation.base

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class BaseViewModel : ViewModel() {

    private val disposable = CompositeDisposable()

    fun Disposable.add() {
        disposable.add(this)
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}