package apoi.mviapp.mvi2.arch

import apoi.mviapp.core.BaseFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

abstract class Mvi2BaseFragment<E : Event, S : State, A : Action, R : Result>
    : Mvi2View<E, S>, BaseFragment() {

    protected abstract val viewModel: ViewModel<E, S, A, R>

    protected val disposables = CompositeDisposable()

    protected fun bindToViewModel() {
        viewModel.processEvents(events)

        disposables.add(viewModel.states()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::render))
    }
}
