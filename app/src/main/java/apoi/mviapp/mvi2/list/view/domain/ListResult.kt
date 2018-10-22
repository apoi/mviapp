package apoi.mviapp.mvi2.list.view.domain

import apoi.mviapp.mvi2.arch.Result

sealed class ListResult : Result {

    object SkipResult : ListResult()
}