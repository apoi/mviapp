package apoi.mviapp.mvi2.domain

import apoi.mviapp.mvi2.arch.Result
import apoi.mviapp.pojo.Photo

sealed class ListResult : Result {
    object SkipResult : ListResult()
    data class ItemLoadSuccess(val photos: List<Photo>) : ListResult()
    data class ItemLoadError(val error: String) : ListResult()
}
