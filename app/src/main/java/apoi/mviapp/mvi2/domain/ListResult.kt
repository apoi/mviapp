package apoi.mviapp.mvi2.domain

import apoi.mviapp.mvi2.arch.Result

sealed class ListResult : Result {

    object SkipResult : ListResult()
}