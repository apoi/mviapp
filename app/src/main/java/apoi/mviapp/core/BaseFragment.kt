package apoi.mviapp.core

import android.os.Bundle
import androidx.fragment.app.Fragment
import apoi.mviapp.injections.FragmentComponent
import apoi.mviapp.injections.FragmentModule

abstract class BaseFragment : Fragment() {

    private var component: FragmentComponent? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        inject()
    }

    fun getComponent(): FragmentComponent {
        if (component == null) {
            component = (activity as BaseActivity).getComponent()
                .plusFragment(FragmentModule(this))
        }

        return component!!
    }

    protected abstract fun inject()
}
