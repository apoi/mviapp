package apoi.mviapp.mvi2.domain

import apoi.mviapp.mvi2.arch.State

sealed class ListState : State {
    data class Error(val error: String) : ListState()
}
