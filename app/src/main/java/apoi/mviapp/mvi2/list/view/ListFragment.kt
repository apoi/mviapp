package apoi.mviapp.mvi2.list.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import apoi.mviapp.R
import apoi.mviapp.extensions.setVisibility
import apoi.mviapp.mvi2.arch.Logger
import apoi.mviapp.mvi2.arch.store.Store
import apoi.mviapp.mvi2.arch.view.Flow
import apoi.mviapp.mvi2.arch.view.MviBaseFragment
import apoi.mviapp.mvi2.arch.viewmodel.BaseViewModel
import apoi.mviapp.mvi2.list.view.domain.ListFragmentViewModel
import apoi.mviapp.mvi2.list.view.domain.ListUiAction
import apoi.mviapp.mvi2.list.view.domain.ListUiEvent
import apoi.mviapp.mvi2.list.view.domain.ListUiModel
import apoi.mviapp.mvi2.list.view.domain.ListUiResult
import apoi.mviapp.mvi2.list.view.domain.dispatcher
import apoi.mviapp.mvi2.list.view.domain.eventMapper
import apoi.mviapp.mvi2.list.view.domain.reducer
import apoi.mviapp.injections.FragmentComponent
import apoi.mviapp.network.Api
import com.jakewharton.rxbinding2.view.clicks
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import kotlinx.android.synthetic.main.list_fragment.*
import javax.inject.Inject

class ListFragment : MviBaseFragment<FragmentComponent, ListUiModel, ListUiEvent, ListFragmentViewModel>() {

    @Inject
    lateinit var api: Api

    private lateinit var photoAdapter: PhotoAdapter

    private lateinit var viewModel: BaseViewModel<ListUiEvent, ListUiAction, ListUiResult, ListUiModel>

    override fun inject() {
        getComponent().inject(this)

        // TODO inject
        viewModel = BaseViewModel(
            ListUiEvent.Initial,
            eventMapper,
            dispatcher(ListLoadInteractor(api)),
            Store(ListUiModel(), reducer, "ListStore", Logger()),
            "ListFragment",
            Logger()
        )

        flow = Flow(this, viewModel, this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.list_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
    }

    private fun initRecyclerView() {
        photoAdapter = PhotoAdapter({})

        recycler_view.apply {
            layoutManager = LinearLayoutManager(context).also {
                addItemDecoration(DividerItemDecoration(context, it.orientation).apply {
                    setDrawable(ContextCompat.getDrawable(context, R.drawable.divider)!!)
                })
            }
            adapter = photoAdapter
            setHasFixedSize(true)
        }
    }

    override fun uiEvents(): LiveData<ListUiEvent> {
        return LiveDataReactiveStreams.fromPublisher(
            Flowable.merge(loadRequested(), photoClicked()))
    }

    private fun loadRequested() = load_button.clicks()
        .map { ListUiEvent.LoadButtonClicked }
        .toFlowable(BackpressureStrategy.BUFFER)

    // TODO photo click
    private fun photoClicked() = Flowable.never<ListUiEvent>()

    override fun render(model: ListUiModel) {
        progress_bar.setVisibility(model.inProgress)
        load_button.setVisibility(!model.inProgress && model.photos.isEmpty())
        recycler_view.setVisibility(!model.inProgress && model.photos.isNotEmpty())

        photoAdapter.setPhotos(model.photos)
    }

    companion object {
        fun create(): ListFragment {
            return ListFragment()
        }
    }
}
