package apoi.mviapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.main_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        button_mobius.setOnClickListener { openFragment(apoi.mviapp.mobius.view.ListFragment()) }
        button_freesound.setOnClickListener { openFragment(apoi.mviapp.freesound.view.ListFragment()) }
        button_mvi2.setOnClickListener { openFragment(apoi.mviapp.mvi2.view.ListFragment()) }
    }

    private fun openFragment(fragment: Fragment) {
        requireFragmentManager()
            .beginTransaction()
            .addToBackStack(fragment.javaClass.simpleName)
            .replace(R.id.container, fragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
    }
}