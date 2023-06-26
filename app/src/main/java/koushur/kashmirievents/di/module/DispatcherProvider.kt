package koushur.kashmirievents.di.module


import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

interface DispatcherProvider {
    fun mainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    fun defaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

    fun ioDispatcher(): CoroutineDispatcher = Dispatchers.IO

}

open class DefaultDispatcherProvider : DispatcherProvider