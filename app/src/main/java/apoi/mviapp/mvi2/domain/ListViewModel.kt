package apoi.mviapp.mvi2.domain

import apoi.mviapp.mvi2.arch.ViewModel
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.functions.BiFunction

class ListViewModel : ViewModel<ListEvent, ListState, ListAction, ListResult>, androidx.lifecycle.ViewModel() {

    lateinit var initialState: ListState

    private val relay: PublishRelay<ListEvent> = PublishRelay.create()

    override fun processEvents(events: Observable<ListEvent>) {
        events.subscribe(relay)
    }

    override val states: Observable<ListState> by lazy(LazyThreadSafetyMode.NONE) {
        relay.compose(eventFilter())
            .map { event: ListEvent -> actionFromEvent(event) }
            .filter { action: ListAction -> action !is ListAction.SkipAction }
            .compose(results())
            .scan(initialState, reducer())
            .replay(1)
            .autoConnect(0)
    }

    override fun eventFilter(): ObservableTransformer<ListEvent, ListEvent> {
        return ObservableTransformer { events -> events }
    }

    override fun actionFromEvent(event: ListEvent): ListAction = when (event) {
        is ListEvent.OnInitialized -> ListAction.SkipAction
    }

    override fun results(): ObservableTransformer<ListAction, ListResult> {
        return ObservableTransformer { actions: Observable<ListAction> ->
            actions.publish { shared: Observable<ListAction> ->
                shared.ofType(ListAction.SkipAction::class.java)
                    .map<ListResult> { ListResult.SkipResult }

            }
        }
    }

    override fun reducer(): BiFunction<ListState, ListResult, ListState> {
        return BiFunction { previousState: ListState, result: ListResult ->
            when (result) {
                is ListResult.SkipResult -> previousState
            }
        }
    }
}
