package apoi.mviapp.mvi2.arch

import apoi.mviapp.common.*
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers

abstract class ViewModel<E : Event, S : State, A : Action, R : Result> : androidx.lifecycle.ViewModel() {

    private val relay: PublishRelay<E> = PublishRelay.create()

    fun states(): Observable<S> {
        return relay.compose(eventFilter())
            .observeOn(Schedulers.io())
            .map(this::actionFromEvent)
            .filter { action -> action !is ListAction.Initial }
            .compose(results())
            .scan(initialState(), reducer())
            .replay(1)
            .autoConnect(0)
    }

    fun processEvents(events: Observable<E>): Disposable {
        return events.subscribe(relay)
    }

    abstract fun initialState(): S

    protected abstract fun eventFilter(): ObservableTransformer<E, E>

    protected abstract fun actionFromEvent(event: E): A

    protected abstract fun results(): ObservableTransformer<A, R>

    protected abstract fun reducer(): BiFunction<S, R, S>
}
