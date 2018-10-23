package apoi.mviapp.freesound.view

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
import apoi.mviapp.common.ListEvent
import apoi.mviapp.common.ListState
import apoi.mviapp.extensions.setVisibility
import apoi.mviapp.freesound.arch.view.Flow
import apoi.mviapp.freesound.arch.view.MviBaseFragment
import apoi.mviapp.freesound.arch.viewmodel.ViewModel
import apoi.mviapp.freesound.domain.ListViewModel
import apoi.mviapp.network.Api
import apoi.mviapp.photo.PhotoAdapter
import apoi.mviapp.pojo.Photo
import com.jakewharton.rxbinding2.view.clicks
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.list_fragment.*
import javax.inject.Inject

class ListFragment : MviBaseFragment<ListState, ListEvent, ViewModel<ListEvent, ListState>>() {

    @Inject
    lateinit var api: Api

    private val photoClickedSubject = PublishSubject.create<Photo>()

    private val photoAdapter = PhotoAdapter(photoClickedSubject::onNext)

    override val viewModel: ViewModel<ListEvent, ListState> by lazy {
        ListViewModel(requireContext(), ListLoadInteractor(api))
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

        flow = Flow(this, viewModel, this)
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

    override fun events(): LiveData<ListEvent> {
        return LiveDataReactiveStreams.fromPublisher(
            Flowable.merge(loadRequested(), photoClicked())
        )
    }

    private fun loadRequested() = load_button.clicks()
        .map { ListEvent.LoadButtonClicked }
        .toFlowable(BackpressureStrategy.BUFFER)

    private fun photoClicked() = photoClickedSubject
        .map { ListEvent.PhotoClicked(it) }
        .toFlowable(BackpressureStrategy.BUFFER)

    override fun render(state: ListState) {
        progress_bar.setVisibility(state.inProgress)
        load_button.setVisibility(!state.inProgress && state.photos.isEmpty())
        recycler_view.setVisibility(!state.inProgress && state.photos.isNotEmpty())

        photoAdapter.setPhotos(state.photos)
    }

    companion object {
        fun create(): ListFragment {
            return ListFragment()
        }
    }
}
