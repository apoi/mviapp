package apoi.mviapp.mvi2.list.view.domain

import apoi.mviapp.mvi2.arch.Action

sealed class ListAction : Action {

    object SkipAction : ListAction()
}