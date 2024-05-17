sealed interface State {
    data object Succeed: State
    data class Failed(val message: String?): State
    data class ErrorUnknown(val message: String): State
    data object ErrorConnectionRefused : State
    data object ErrorResponseNull: State
}

sealed interface LoadingState {
    data object Waiting: LoadingState
    data object Loading: LoadingState
    data class Loaded(val state: State): LoadingState
}
