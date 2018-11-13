package apoi.mviapp.freesound.view

import android.os.Bundle
import androidx.fragment.app.FragmentTransaction
import apoi.mviapp.R
import apoi.mviapp.core.BaseActivity

class ListActivity : BaseActivity() {

    override fun inject() {
        getComponent().inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.list_activity)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, ListFragment())
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit()
        }
    }
}
