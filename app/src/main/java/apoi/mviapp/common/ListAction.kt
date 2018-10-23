package apoi.mviapp.common

import apoi.mviapp.pojo.Photo

sealed class ListAction : Action {
    object Initial : ListAction()
    object LoadContent : ListAction()
    data class ShowError(val error: String) : ListAction()
    data class ShowPhoto(val photo: Photo) : ListAction()
}
