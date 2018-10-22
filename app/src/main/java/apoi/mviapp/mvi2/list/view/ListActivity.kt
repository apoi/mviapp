package apoi.mviapp.mvi2.list.view

import android.os.Bundle
import androidx.fragment.app.FragmentTransaction
import apoi.mviapp.R
import apoi.mviapp.core.BaseActivity
import apoi.mviapp.mobius.list.domain.ListEvent
import apoi.mviapp.mvi2.arch.Mvi2Activity
import apoi.mviapp.mvi2.list.view.domain.ListViewModel
import apoi.mviapp.mvi2.list.view.domain.ListViewState

class ListActivity : Mvi2Activity<ListEvent, ListViewState, ListViewModel>() {

    override fun inject() {
        getComponent().inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, ListFragment())
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
    }
}
