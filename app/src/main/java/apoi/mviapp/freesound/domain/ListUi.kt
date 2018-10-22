package apoi.mviapp.freesound.domain

import android.content.Context
import android.content.Intent
import apoi.mviapp.freesound.arch.Action
import apoi.mviapp.freesound.arch.Dispatcher
import apoi.mviapp.freesound.arch.Event
import apoi.mviapp.freesound.arch.EventMapper
import apoi.mviapp.freesound.arch.Result
import apoi.mviapp.freesound.arch.State
import apoi.mviapp.freesound.arch.combine
import apoi.mviapp.freesound.arch.viewmodel.BaseViewModel
import apoi.mviapp.freesound.view.ListLoadInteractor
import apoi.mviapp.photo.PHOTO
import apoi.mviapp.photo.PhotoActivity
import apoi.mviapp.pojo.Photo

typealias ListFragmentViewModel = BaseViewModel<ListEvent, ListAction, ListResult, ListState>

sealed class ListEvent : Event {
    object Initial : ListEvent()
    object LoadButtonClicked : ListEvent()
    data class PhotoClicked(val photo: Photo) : ListEvent()
}

sealed class ListResult : Result {
    object NoChange : ListResult()
    data class ItemLoadSuccess(val photos: List<Photo>) : ListResult()
    data class ItemLoadError(val error: String) : ListResult()
}

sealed class ListAction : Action {
    object Initial : ListAction()
    object LoadContent : ListAction()
    data class ShowError(val error: String) : ListAction()
    data class ShowPhoto(val photo: Photo) : ListAction()
}

data class ListState(
    val inProgress: Boolean = false,
    val photos: List<Photo> = emptyList()
) : State

val eventMapper: EventMapper<ListEvent, ListAction> =
    { event: ListEvent ->
        when (event) {
            is ListEvent.Initial -> ListAction.Initial
            is ListEvent.LoadButtonClicked -> ListAction.LoadContent
            is ListEvent.PhotoClicked -> ListAction.ShowPhoto(event.photo)
        }
    }

fun dispatcher(context: Context, listLoadInteractor: ListLoadInteractor): Dispatcher<ListAction, ListResult> {

    val initial = Dispatcher<ListAction, ListResult> {
        it.ofType(ListAction.Initial::class.java)
            .map { ListResult.NoChange }
    }

    val loadList = Dispatcher<ListAction, ListResult> {
        it.ofType(ListAction.LoadContent::class.java)
            .flatMap { listLoadInteractor.loadList().toFlowable() }
            .map<ListResult> { ListResult.ItemLoadSuccess(it) }
            .onErrorReturn { ListResult.ItemLoadError("Error!") }
    }

    val showPhoto = Dispatcher<ListAction, ListResult> {
        it.ofType(ListAction.ShowPhoto::class.java)
            .doOnNext {
                context.startActivity(Intent(context, PhotoActivity::class.java).apply {
                    putExtra(PHOTO, it.photo.url)
                })
            }
            .map { ListResult.NoChange }
    }

    return combine(initial, loadList, showPhoto)
}

