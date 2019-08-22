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
import apoi.mviapp.network.Api
import apoi.mviapp.photo.PHOTO
import apoi.mviapp.photo.PhotoActivity

class ListViewModel(
    context: Context,
    api: Api
) : BaseViewModel<ListEvent, ListAction, ListResult, ListState>() {

    override val initialEvent = ListEvent.Initial

    override val eventMapper: EventMapper<ListEvent, ListAction> =
        { event: ListEvent ->
            when (event) {
                is ListEvent.Initial -> ListAction.Initial
                is ListEvent.LoadButtonClicked -> ListAction.LoadContent
                is ListEvent.PullToRefresh -> ListAction.LoadContent
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
                    api.getPhotos()
                        .toFlowable()
                        .map<ListResult> { ListResult.ItemLoadSuccess(it) }
                        .onErrorReturn { ListResult.ItemLoadError(it.toString()) }
                        // Add finishing progress as additional emit, start with partial progress
                        .flatMapIterable { listOf(it, ListResult.ItemLoadProgress(1f)) }
                        .startWith(ListResult.ItemLoadProgress(0.5f))
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
                is ListResult.ItemLoadProgress -> current.copy(inProgress = result.progress < 1f)
                is ListResult.ItemLoadSuccess -> current.copy(photos = result.photos)
                is ListResult.ItemLoadError -> current
            }
        }

    override val store = Store({ state().value ?: ListState() }, reducer)
}
