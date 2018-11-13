package apoi.mviapp.mvi2.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import apoi.mviapp.R
import apoi.mviapp.common.ListAction
import apoi.mviapp.common.ListEvent
import apoi.mviapp.common.ListResult
import apoi.mviapp.common.ListState
import apoi.mviapp.extensions.setVisibility
import apoi.mviapp.mvi2.arch.Mvi2BaseFragment
import apoi.mviapp.mvi2.arch.ViewModel
import apoi.mviapp.mvi2.domain.ListViewModel
import apoi.mviapp.network.Api
import apoi.mviapp.photo.PhotoAdapter
import apoi.mviapp.pojo.Photo
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.list_fragment.*
import javax.inject.Inject

class ListFragment : Mvi2BaseFragment<ListEvent, ListState, ListAction, ListResult>() {

    @Inject
    lateinit var api: Api

    private val photoClickedSubject = PublishSubject.create<Photo>()

    private val photoAdapter = PhotoAdapter(photoClickedSubject::onNext)

    private val onInitialisedEvent: PublishRelay<ListEvent.Initial> = PublishRelay.create()

    override val viewModel: ViewModel<ListEvent, ListState, ListAction, ListResult> by lazy {
        ListViewModel(requireContext(), api, ListState())
    }

    override val events: Observable<ListEvent> by lazy {
        Observable.merge(onInitialisedEvent, loadRequested(), photoClicked())
    }

    override fun inject() {
        getComponent().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.list_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        bindToViewModel()
    }

    private fun initRecyclerView() {
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

    private fun loadRequested() = load_button.clicks()
        .map { ListEvent.LoadButtonClicked }

    private fun photoClicked() = photoClickedSubject
        .map { ListEvent.PhotoClicked(it) }

    override fun render(state: ListState) {
        progress_bar.setVisibility(state.inProgress)
        load_button.setVisibility(!state.inProgress && state.photos.isEmpty())
        recycler_view.setVisibility(!state.inProgress && state.photos.isNotEmpty())

        photoAdapter.setPhotos(state.photos)
    }
}
