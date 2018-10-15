package apoi.mviapp.freesound.list.view

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
import apoi.mviapp.freesound.arch.view.MviBaseFragment
import apoi.mviapp.freesound.list.view.domain.ListFragmentViewModel
import apoi.mviapp.freesound.list.view.domain.ListUiEvent
import apoi.mviapp.freesound.list.view.domain.ListUiModel
import apoi.mviapp.injections.FragmentComponent
import apoi.mviapp.pojo.Photo
import com.jakewharton.rxbinding2.view.clicks
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import kotlinx.android.synthetic.main.list_fragment.*

class ListFragment : MviBaseFragment<FragmentComponent, ListUiModel, ListUiEvent, ListFragmentViewModel>() {

    private lateinit var photoAdapter: PhotoAdapter

    override fun inject() {
        getComponent().inject(this)
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
    private fun photoClicked() = load_button.clicks()
        .map { ListUiEvent.PhotoClicked(Photo("", 0, "", "", "")) }
        .toFlowable(BackpressureStrategy.BUFFER)

    override fun render(model: ListUiModel) {
        progress_bar.setVisibility(model.inProgress)
        load_button.setVisibility(!model.inProgress && model.photos.isEmpty())
        recycler_view.setVisibility(!model.inProgress && model.photos.isNotEmpty())

        photoAdapter.setPhotos(model.photos)
    }
}
