package koushur.kashmirievents.presentation.common

sealed class ResultState<T : Any> {
    class Success<T : Any>(val data: T) : ResultState<T>()
    class Error<T : Any>(val exception: Throwable) : ResultState<T>()
}