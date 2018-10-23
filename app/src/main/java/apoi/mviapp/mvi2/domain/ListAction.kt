package apoi.mviapp.mvi2.domain

import apoi.mviapp.mvi2.arch.Action
import apoi.mviapp.pojo.Photo

sealed class ListAction : Action {
    object SkipAction : ListAction()
    object LoadContent : ListAction()
    data class ShowError(val error: String) : ListAction()
    data class ShowPhoto(val photo: Photo) : ListAction()
}
