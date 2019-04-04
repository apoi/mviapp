package apoi.mviapp.common

import apoi.mviapp.pojo.Photo

sealed class ListResult : Result {
    object NoChange : ListResult()
    object Loading : ListResult()
    data class ItemLoadSuccess(val photos: List<Photo>) : ListResult()
    data class ItemLoadError(val error: String) : ListResult()
}
