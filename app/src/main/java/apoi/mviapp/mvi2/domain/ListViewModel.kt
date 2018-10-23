package apoi.mviapp.mvi2.domain

import android.content.Context
import android.content.Intent
import apoi.mviapp.mvi2.arch.ViewModel
import apoi.mviapp.network.Api
import apoi.mviapp.photo.PHOTO
import apoi.mviapp.photo.PhotoActivity
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.functions.BiFunction
import timber.log.Timber

class ListViewModel(
    private val context: Context,
    private val api: Api,
    private val initialState: ListState
) : ViewModel<ListEvent, ListState, ListAction, ListResult>() {

    override fun initialState(): ListState = initialState

    override fun eventFilter(): ObservableTransformer<ListEvent, ListEvent> {
        return ObservableTransformer { events -> events }
    }

    override fun actionFromEvent(event: ListEvent): ListAction = when (event) {
        is ListEvent.OnInitialized -> ListAction.SkipAction
        is ListEvent.LoadButtonClicked -> ListAction.LoadContent
        is ListEvent.PhotoClicked -> ListAction.ShowPhoto(event.photo)
    }

    override fun results(): ObservableTransformer<ListAction, ListResult> {
        return ObservableTransformer { actions: Observable<ListAction> ->
            actions.publish { shared: Observable<ListAction> ->
                Observable.merge(
                    shared.ofType(ListAction.SkipAction::class.java)
                        .doOnNext { Timber.w("Skip") }
                        .map<ListResult> { ListResult.SkipResult },

                    shared.ofType(ListAction.LoadContent::class.java)
                        .doOnNext { Timber.w("Load") }
                        .flatMap { api.getPhotos().toObservable() }
                        .doOnNext { Timber.w("Result $it") }
                        .map<ListResult> { ListResult.ItemLoadSuccess(it) }
                        .onErrorReturn { ListResult.ItemLoadError(it.toString()) },

                    shared.ofType(ListAction.ShowPhoto::class.java)
                        .doOnNext { Timber.w("Photo") }
                        .doOnNext {
                            context.startActivity(Intent(context, PhotoActivity::class.java).apply {
                                putExtra(PHOTO, it.photo.url)
                            })
                        }
                        .map { ListResult.SkipResult }
                )
            }
        }
    }

    override fun reducer(): BiFunction<ListState, ListResult, ListState> {
        return BiFunction { previousState: ListState, result: ListResult ->
            when (result) {
                is ListResult.SkipResult -> previousState
                is ListResult.ItemLoadSuccess -> previousState.copy(inProgress = false, photos = result.photos)
                is ListResult.ItemLoadError -> previousState.copy(inProgress = false)
            }
        }
    }
}
