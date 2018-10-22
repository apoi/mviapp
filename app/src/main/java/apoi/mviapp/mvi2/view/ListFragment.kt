package apoi.mviapp.mvi2.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import apoi.mviapp.R
import apoi.mviapp.mvi2.arch.Mvi2BaseFragment
import apoi.mviapp.mvi2.domain.ListAction
import apoi.mviapp.mvi2.domain.ListEvent
import apoi.mviapp.mvi2.domain.ListResult
import apoi.mviapp.mvi2.domain.ListState
import apoi.mviapp.network.Api
import apoi.mviapp.photo.PhotoAdapter
import io.reactivex.Observable
import kotlinx.android.synthetic.main.list_fragment.*
import javax.inject.Inject

class ListFragment : Mvi2BaseFragment<ListEvent, ListState, ListAction, ListResult>() {

    @Inject
    lateinit var api: Api

    private lateinit var photoAdapter: PhotoAdapter

    override val events: Observable<ListEvent>
        get() = TODO("not implemented")

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

    override fun render(state: ListState) {
        TODO("not implemented")
    }
}
