package apoi.mviapp.mobius.photo.view

import android.os.Bundle
import androidx.fragment.app.FragmentTransaction
import apoi.mviapp.R
import apoi.mviapp.core.BaseActivity

class PhotoActivity : BaseActivity() {

    override fun inject() {
        getComponent().inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, PhotoFragment())
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
    }
}
