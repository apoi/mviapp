package apoi.mviapp.freesound.list.view.domain

import apoi.mviapp.freesound.arch.Action
import apoi.mviapp.freesound.arch.Event
import apoi.mviapp.freesound.arch.Result
import apoi.mviapp.freesound.arch.State
import apoi.mviapp.freesound.arch.viewmodel.BaseViewModel
import apoi.mviapp.pojo.Photo

typealias ListFragmentViewModel = BaseViewModel<ListUiEvent, ListUiAction, ListUiResult, ListUiModel>

sealed class ListUiEvent : Event {
    object LoadButtonClicked : ListUiEvent()
    data class PhotoClicked(val photo: Photo) : ListUiEvent()
}

sealed class ListUiResult : Result {
    object NoChange : ListUiResult()
    data class ItemLoadSuccess(val photos: List<Photo>) : ListUiResult()
    data class ItemLoadError(val error: String) : ListUiResult()
}

sealed class ListUiAction : Action {
    data class ShowError(val error: String) : ListUiAction()
    data class ShowPhoto(val photo: Photo) : ListUiAction()
}

data class ListUiModel(
    val inProgress: Boolean = false,
    val photos: List<Photo> = emptyList()
) : State
