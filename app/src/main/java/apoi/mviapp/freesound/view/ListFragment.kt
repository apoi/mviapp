package apoi.mviapp.freesound.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import apoi.mviapp.common.ListEvent
import apoi.mviapp.common.ListState
import apoi.mviapp.extensions.viewModelProvider
import apoi.mviapp.freesound.arch.view.Flow
import apoi.mviapp.freesound.arch.view.MviBaseFragment
import apoi.mviapp.freesound.arch.viewmodel.ViewModel
import apoi.mviapp.freesound.domain.ListViewModel
import apoi.mviapp.network.Api
import javax.inject.Inject

class ListFragment : MviBaseFragment<ListState, ListEvent, ViewModel<ListEvent, ListState>>() {

    @Inject
    lateinit var api: Api

    private lateinit var view: ListView

    override val viewModel: ViewModel<ListEvent, ListState> by lazy {
        this.viewModelProvider {
            ListViewModel(requireContext(), api, initialState ?: ListState())
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

        // Flow can be created after injections.
        flow = Flow(view, viewModel, this)
    }
}
