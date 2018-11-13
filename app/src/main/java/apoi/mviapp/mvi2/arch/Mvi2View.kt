package apoi.mviapp.mvi2.arch

import apoi.mviapp.common.Event
import apoi.mviapp.common.State
import io.reactivex.Observable

interface Mvi2View<E : Event, in S : State> {

    val events: Observable<E>

    fun render(state: S)
}
