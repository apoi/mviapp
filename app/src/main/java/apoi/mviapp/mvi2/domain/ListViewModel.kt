package apoi.mviapp.mvi2.domain

import android.content.Context
import android.content.Intent
import apoi.mviapp.common.Constants
import apoi.mviapp.common.ListAction
import apoi.mviapp.common.ListEvent
import apoi.mviapp.common.ListResult
import apoi.mviapp.common.ListState
import apoi.mviapp.mvi2.arch.ViewModel
import apoi.mviapp.network.Api
import apoi.mviapp.network.ErrorMapper
import apoi.mviapp.photo.PHOTO
import apoi.mviapp.photo.PhotoActivity
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.functions.BiFunction
import timber.log.Timber
import java.util.concurrent.TimeUnit

class ListViewModel(
    private val context: Context,
    private val api: Api
) : ViewModel<ListEvent, ListState, ListAction, ListResult>() {

    override fun initialState(): ListState = state().value ?: ListState()

    override fun eventFilter(): ObservableTransformer<ListEvent, ListEvent> {
        return ObservableTransformer { events -> events }
    }

    override fun actionFromEvent(event: ListEvent): ListAction = when (event) {
        is ListEvent.Initial -> ListAction.Initial
        is ListEvent.LoadButtonClicked -> ListAction.LoadContent
        is ListEvent.PullToRefresh -> ListAction.LoadContent
        is ListEvent.PhotoClicked -> ListAction.ShowPhoto(event.photo)
    }

    override fun results(): ObservableTransformer<ListAction, ListResult> {
        return ObservableTransformer { actions: Observable<ListAction> ->
            actions.publish { shared: Observable<ListAction> ->
                Observable.merge(
                    shared.ofType(ListAction.Initial::class.java)
                        .doOnNext { Timber.w("Skip") }
                        .map<ListResult> { ListResult.NoChange },

                    shared.ofType(ListAction.LoadContent::class.java)
                        .doOnNext { Timber.w("Load") }
                        .switchMap {
                            api.getPhotos()
                                .toObservable()
                                .map<ListResult> { ListResult.ItemLoadSuccess(it) }
                                .onErrorResumeNext { error: Throwable ->
                                    Observable.timer(Constants.ERROR_SHOW_DURATION, TimeUnit.SECONDS)
                                        .map<ListResult> { ListResult.ItemLoadErrorClear }
                                        .startWith(ListResult.ItemLoadErrorShow(error))
                                }
                                // Add finishing progress as additional emit, start with partial progress
                                .flatMapIterable { listOf(it, ListResult.ItemLoadProgress(1f)) }
                                .startWith(ListResult.ItemLoadProgress(0.5f))
                        }
                        .doOnNext { Timber.w("Result $it") },

                    shared.ofType(ListAction.ShowPhoto::class.java)
                        .doOnNext { Timber.w("Photo") }
                        .doOnNext {
                            context.startActivity(Intent(context, PhotoActivity::class.java).apply {
                                putExtra(PHOTO, it.photo.url)
                            })
                        }
                        .map { ListResult.NoChange }
                )
            }
        }
    }

    override fun reducer(): BiFunction<ListState, ListResult, ListState> {
        return BiFunction { previousState: ListState, result: ListResult ->
            when (result) {
                is ListResult.NoChange -> previousState
                is ListResult.ItemLoadProgress -> previousState.copy(inProgress = result.progress < 1f)
                is ListResult.ItemLoadSuccess -> previousState.copy(photos = result.photos)
                is ListResult.ItemLoadErrorShow -> previousState.copy(error = ErrorMapper(context, result.error).errorToMessage())
                is ListResult.ItemLoadErrorClear -> previousState.copy(error = null)
            }
        }
    }
}
