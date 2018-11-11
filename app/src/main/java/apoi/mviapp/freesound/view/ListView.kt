package apoi.mviapp.freesound.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import apoi.mviapp.R
import apoi.mviapp.common.ListEvent
import apoi.mviapp.common.ListEvent.LoadButtonClicked
import apoi.mviapp.common.ListEvent.PhotoClicked
import apoi.mviapp.common.ListState
import apoi.mviapp.extensions.setVisibility
import apoi.mviapp.freesound.arch.view.MviView
import apoi.mviapp.photo.PhotoAdapter
import apoi.mviapp.pojo.Photo
import com.jakewharton.rxbinding2.view.clicks
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.subjects.PublishSubject

class ListView(
    inflater: LayoutInflater,
    parent: ViewGroup?
): MviView<ListEvent, ListState> {

    val view: View = inflater.inflate(R.layout.list_fragment, parent, false)

    private val progressBar = view.findViewById<ProgressBar>(R.id.progress_bar)
    private val loadButton = view.findViewById<Button>(R.id.load_button)
    private val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)

    private val photoClickedSubject = PublishSubject.create<Photo>()
    private val photoAdapter: PhotoAdapter = PhotoAdapter(photoClickedSubject::onNext)

    init {
        recyclerView.apply {
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
            Flowable.merge(photoClickedStream(), loadClickedStream())
        )
    }

    private fun photoClickedStream(): Flowable<PhotoClicked> {
        return photoClickedSubject
            .map { PhotoClicked(it) }
            .toFlowable(BackpressureStrategy.BUFFER)
    }

    private fun loadClickedStream(): Flowable<LoadButtonClicked> {
        return loadButton.clicks()
            .map { LoadButtonClicked }
            .toFlowable(BackpressureStrategy.BUFFER)
    }

    override fun render(state: ListState) {
        progressBar.setVisibility(state.inProgress)
        loadButton.setVisibility(!state.inProgress && state.photos.isEmpty())
        recyclerView.setVisibility(!state.inProgress && state.photos.isNotEmpty())

        photoAdapter.setPhotos(state.photos)
    }
}
