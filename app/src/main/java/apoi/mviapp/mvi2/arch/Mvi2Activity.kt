package apoi.mviapp.mvi2.arch

import apoi.mviapp.core.BaseActivity
import io.reactivex.disposables.CompositeDisposable

abstract class Mvi2Activity<E : Event, VS : ViewState, VM : ViewModel<E, VS, Action, Result>>
    : Mvi2View<E, VS>, BaseActivity() {

    protected lateinit var viewModel: VM

    protected val disposables = CompositeDisposable()

    protected fun bindToViewModel() {
        viewModel.processEvents(events)
        disposables.add(viewModel.states.subscribe(this::render))
    }
}
