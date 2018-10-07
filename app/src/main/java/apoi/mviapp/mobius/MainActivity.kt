package apoi.mviapp.mobius

import android.os.Bundle
import androidx.fragment.app.FragmentTransaction
import apoi.mviapp.R
import apoi.mviapp.core.BaseActivity

class MainActivity : BaseActivity() {

    override fun inject() {
        getComponent().inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.main_activity)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, MainFragment())
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
    }
}
