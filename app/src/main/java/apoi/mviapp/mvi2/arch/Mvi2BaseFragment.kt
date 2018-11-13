package apoi.mviapp.mvi2.arch

import apoi.mviapp.common.Action
import apoi.mviapp.common.Event
import apoi.mviapp.common.Result
import apoi.mviapp.common.State
import apoi.mviapp.core.BaseFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

abstract class Mvi2BaseFragment<E : Event, S : State, A : Action, R : Result>
    : BaseFragment() {

    protected abstract val viewModel: ViewModel<E, S, A, R>

    protected val disposables = CompositeDisposable()

    protected fun bindToViewModel(view: Mvi2View<E, S>) {
        viewModel.processEvents(view.events)
            .also { disposables.add(it) }

        viewModel.states()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(view::render)
            .also { disposables.add(it) }
    }
}
