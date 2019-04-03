package apoi.mviapp.mvi2.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import apoi.mviapp.common.ListAction
import apoi.mviapp.common.ListEvent
import apoi.mviapp.common.ListResult
import apoi.mviapp.common.ListState
import apoi.mviapp.extensions.viewModelProvider
import apoi.mviapp.mvi2.arch.Mvi2BaseFragment
import apoi.mviapp.mvi2.arch.ViewModel
import apoi.mviapp.mvi2.domain.ListViewModel
import apoi.mviapp.network.Api
import javax.inject.Inject

class ListFragment : Mvi2BaseFragment<ListEvent, ListState, ListAction, ListResult>() {

    @Inject
    lateinit var api: Api

    private lateinit var view: ListView

    override val viewModel: ViewModel<ListEvent, ListState, ListAction, ListResult> by lazy {
        this.viewModelProvider {
            ListViewModel(requireContext(), api)
        }
    }

    override fun inject() {
        getComponent().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return ListView(inflater, container).also {
            view = it
        }.view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        bindToViewModel(view)
    }
}
