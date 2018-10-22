package apoi.mviapp.freesound.domain

import apoi.mviapp.freesound.arch.Action
import apoi.mviapp.freesound.arch.Dispatcher
import apoi.mviapp.freesound.arch.Event
import apoi.mviapp.freesound.arch.EventMapper
import apoi.mviapp.freesound.arch.Result
import apoi.mviapp.freesound.arch.State
import apoi.mviapp.freesound.arch.combine
import apoi.mviapp.freesound.arch.viewmodel.BaseViewModel
import apoi.mviapp.freesound.view.ListLoadInteractor
import apoi.mviapp.pojo.Photo

typealias ListFragmentViewModel = BaseViewModel<ListUiEvent, ListUiAction, ListUiResult, ListUiModel>

sealed class ListUiEvent : Event {
    object Initial : ListUiEvent()
    object LoadButtonClicked : ListUiEvent()
    data class PhotoClicked(val photo: Photo) : ListUiEvent()
}

sealed class ListUiResult : Result {
    object NoChange : ListUiResult()
    data class ItemLoadSuccess(val photos: List<Photo>) : ListUiResult()
    data class ItemLoadError(val error: String) : ListUiResult()
}

sealed class ListUiAction : Action {
    object Initial : ListUiAction()
    object LoadContent : ListUiAction()
    data class ShowError(val error: String) : ListUiAction()
    data class ShowPhoto(val photo: Photo) : ListUiAction()
}

data class ListUiModel(
    val inProgress: Boolean = false,
    val photos: List<Photo> = emptyList()
) : State

val eventMapper: EventMapper<ListUiEvent, ListUiAction> =
    { uiEvent: ListUiEvent ->
        when (uiEvent) {
            is ListUiEvent.Initial -> ListUiAction.Initial
            is ListUiEvent.LoadButtonClicked -> ListUiAction.LoadContent
            is ListUiEvent.PhotoClicked -> ListUiAction.ShowPhoto(uiEvent.photo)
        }
    }

fun dispatcher(listLoadInteractor: ListLoadInteractor): Dispatcher<ListUiAction, ListUiResult> {

    val initial = Dispatcher<ListUiAction, ListUiResult> {
        it.ofType(ListUiAction.Initial::class.java)
            .map { ListUiResult.NoChange }
    }

    val loadList = Dispatcher<ListUiAction, ListUiResult> {
        it.ofType(ListUiAction.LoadContent::class.java)
            .flatMap { listLoadInteractor.loadList().toFlowable() }
            .map { ListUiResult.ItemLoadSuccess(it) }
    }

    return combine(initial, loadList)
}

