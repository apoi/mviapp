package apoi.mviapp.mvi2.arch

import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.functions.BiFunction

interface ViewModel<E : Event, S : State, A : Action, R : Result> {

    val states: Observable<S>

    fun processEvents(events: Observable<E>)

    fun eventFilter(): ObservableTransformer<E, E>

    fun actionFromEvent(event: E): A

    fun results(): ObservableTransformer<A, R>

    fun reducer(): BiFunction<S, R, S>
}
