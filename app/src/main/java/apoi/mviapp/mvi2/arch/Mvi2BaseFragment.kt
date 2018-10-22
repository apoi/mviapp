package apoi.mviapp.mvi2.arch

import apoi.mviapp.core.BaseFragment
import io.reactivex.disposables.CompositeDisposable

abstract class Mvi2BaseFragment<E : Event, S : State, A : Action, R : Result>
    : Mvi2View<E, S>, BaseFragment() {

    protected lateinit var viewModel: ViewModel<E, S, A, R>

    protected val disposables = CompositeDisposable()

    protected fun bindToViewModel() {
        viewModel.processEvents(events)
        disposables.add(viewModel.states.subscribe(this::render))
    }
}
