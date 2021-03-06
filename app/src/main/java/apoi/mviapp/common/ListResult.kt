package apoi.mviapp.common

import apoi.mviapp.pojo.Photo

sealed class ListResult : Result {
    object NoChange : ListResult()
    data class ItemLoadProgress(val progress: Float) : ListResult()
    data class ItemLoadSuccess(val photos: List<Photo>) : ListResult()
    data class ItemLoadErrorShow(val error: Throwable) : ListResult()
    object ItemLoadErrorClear : ListResult()
}
