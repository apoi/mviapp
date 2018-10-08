package apoi.mviapp.injections

import apoi.mviapp.mobius.list.view.ListFragment
import dagger.Subcomponent

@FragmentScope
@Subcomponent(modules = [FragmentModule::class])
interface FragmentComponent {

    fun inject(fragment: ListFragment)
}
