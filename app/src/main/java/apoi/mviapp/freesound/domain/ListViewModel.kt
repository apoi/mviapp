package apoi.mviapp.freesound.domain

import android.content.Context
import android.content.Intent
import apoi.mviapp.common.ListAction
import apoi.mviapp.common.ListEvent
import apoi.mviapp.common.ListResult
import apoi.mviapp.common.ListState
import apoi.mviapp.freesound.arch.Dispatcher
import apoi.mviapp.freesound.arch.EventMapper
import apoi.mviapp.freesound.arch.Reducer
import apoi.mviapp.freesound.arch.combine
import apoi.mviapp.freesound.arch.store.Store
import apoi.mviapp.freesound.arch.viewmodel.BaseViewModel
import apoi.mviapp.network.PhotoService
import apoi.mviapp.photo.PHOTO
import apoi.mviapp.photo.PhotoActivity

class ListViewModel(
    context: Context,
    service: PhotoService,
    initialState: ListState
) : BaseViewModel<ListEvent, ListAction, ListResult, ListState>() {

    override val initialEvent = ListEvent.Initial

    override val eventMapper: EventMapper<ListEvent, ListAction> =
        { event: ListEvent ->
            when (event) {
                is ListEvent.Initial -> ListAction.Initial
                is ListEvent.LoadButtonClicked -> ListAction.LoadContent
                is ListEvent.PhotoClicked -> ListAction.ShowPhoto(event.photo)
            }
        }

    override val dispatcher: Dispatcher<ListAction, ListResult> = combine(
        Dispatcher {
            it.ofType(ListAction.Initial::class.java)
                .map { ListResult.NoChange }
        },
        Dispatcher {
            it.ofType(ListAction.LoadContent::class.java)
                .flatMap {
                    service.getPhotos().toFlowable()
                        .map<ListResult> { ListResult.ItemLoadSuccess(it) }
                        .onErrorReturn { ListResult.ItemLoadError("Error!") }
                        .startWith(ListResult.Loading)
                }
        },
        Dispatcher {
            it.ofType(ListAction.ShowPhoto::class.java)
                .doOnNext {
                    context.startActivity(Intent(context, PhotoActivity::class.java).apply {
                        putExtra(PHOTO, it.photo.url)
                    })
                }
                .map { ListResult.NoChange }
        })

    private val reducer: Reducer<ListResult, ListState> =
        { current: ListState, result: ListResult ->
            when (result) {
                is ListResult.NoChange -> current
                is ListResult.Loading -> current.copy(inProgress = true)
                is ListResult.ItemLoadSuccess -> current.copy(inProgress = false, photos = result.photos)
                is ListResult.ItemLoadError -> current.copy(inProgress = false)
            }
        }

    override val store = Store({ state().value ?: initialState }, reducer)
}
