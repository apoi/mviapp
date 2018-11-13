package apoi.mviapp.mvi2.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import apoi.mviapp.R
import apoi.mviapp.common.ListEvent
import apoi.mviapp.common.ListState
import apoi.mviapp.extensions.setVisibility
import apoi.mviapp.mvi2.arch.Mvi2View
import apoi.mviapp.photo.PhotoAdapter
import apoi.mviapp.pojo.Photo
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class ListView(
    inflater: LayoutInflater,
    parent: ViewGroup?
): Mvi2View<ListEvent, ListState> {

    val view: View = inflater.inflate(R.layout.list_fragment, parent, false)

    private val progressBar = view.findViewById<ProgressBar>(R.id.progress_bar)
    private val loadButton = view.findViewById<Button>(R.id.load_button)
    private val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)

    private val photoClickedSubject = PublishSubject.create<Photo>()
    private val photoAdapter: PhotoAdapter = PhotoAdapter(photoClickedSubject::onNext)

    private val onInitialisedEvent: PublishRelay<ListEvent.Initial> = PublishRelay.create()

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

    override val events: Observable<ListEvent> by lazy {
        Observable.merge(onInitialisedEvent, loadClickedStream(), photoClickedStream())
    }

    private fun loadClickedStream(): Observable<ListEvent.LoadButtonClicked> {
        return loadButton.clicks()
            .map { ListEvent.LoadButtonClicked }
    }

    private fun photoClickedStream(): Observable<ListEvent.PhotoClicked> {
        return photoClickedSubject
            .map { ListEvent.PhotoClicked(it) }
    }

    override fun render(state: ListState) {
        progressBar.setVisibility(state.inProgress)
        loadButton.setVisibility(!state.inProgress && state.photos.isEmpty())
        recyclerView.setVisibility(!state.inProgress && state.photos.isNotEmpty())

        photoAdapter.setPhotos(state.photos)
    }
}
