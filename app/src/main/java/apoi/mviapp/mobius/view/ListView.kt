package apoi.mviapp.mobius.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import apoi.mviapp.R
import apoi.mviapp.common.ListState
import apoi.mviapp.extensions.setVisibility
import apoi.mviapp.mobius.domain.ListEvent
import apoi.mviapp.mobius.domain.LoadButtonClicked
import apoi.mviapp.mobius.domain.PhotoClicked
import apoi.mviapp.mobius.domain.PullToRefresh
import apoi.mviapp.photo.PhotoAdapter
import com.spotify.mobius.Connectable
import com.spotify.mobius.Connection
import com.spotify.mobius.functions.Consumer

class ListView(
    inflater: LayoutInflater,
    parent: ViewGroup?
) : Connectable<ListState, ListEvent> {

    val view: View = inflater.inflate(R.layout.list_fragment, parent, false)

    private val loadButton = view.findViewById<Button>(R.id.load_button)
    private val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
    private val swipeLayout = view.findViewById<SwipeRefreshLayout>(R.id.swipe_layout)

    private lateinit var photoAdapter: PhotoAdapter

    override fun connect(output: Consumer<ListEvent>): Connection<ListState> {
        initRecyclerView(output)
        setListeners(output)

        return object : Connection<ListState> {
            override fun accept(value: ListState) {
                render(value)
            }

            override fun dispose() {
                clearListeners()
            }
        }
    }

    private fun initRecyclerView(output: Consumer<ListEvent>) {
        photoAdapter = PhotoAdapter { output.accept(PhotoClicked(it)) }

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

    private fun setListeners(output: Consumer<ListEvent>) {
        loadButton.setOnClickListener { output.accept(LoadButtonClicked()) }
        swipeLayout.setOnRefreshListener { output.accept(PullToRefresh()) }
    }

    private fun clearListeners() {
        loadButton.setOnClickListener(null)
    }

    fun render(value: ListState) {
        swipeLayout.isRefreshing = value.inProgress
        loadButton.setVisibility(!value.inProgress && value.photos.isEmpty())
        recyclerView.setVisibility(!value.inProgress && value.photos.isNotEmpty())

        photoAdapter.setPhotos(value.photos)
    }
}
