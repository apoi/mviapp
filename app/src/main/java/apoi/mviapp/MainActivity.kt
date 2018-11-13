package apoi.mviapp

import android.os.Bundle
import androidx.fragment.app.FragmentTransaction
import apoi.mviapp.core.BaseActivity

class MainActivity : BaseActivity() {

    override fun inject() {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.main_activity)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .addToBackStack("main")
                .replace(R.id.container, MainFragment())
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit()
        }
    }
}
