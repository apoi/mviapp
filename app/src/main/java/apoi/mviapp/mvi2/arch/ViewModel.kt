package apoi.mviapp.mvi2.arch

import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.functions.BiFunction

interface ViewModel<
    Event : apoi.mviapp.mvi2.arch.Event,
    State : ViewState,
    Action : apoi.mviapp.mvi2.arch.Action,
    Result : apoi.mviapp.mvi2.arch.Result
    > {

    val states: Observable<State>

    fun processEvents(events: Observable<Event>)

    fun eventFilter(): ObservableTransformer<Event, Event>

    fun actionFromEvent(event: Event): Action

    fun results(): ObservableTransformer<Action, Result>

    fun reducer(): BiFunction<State, Result, State>
}
