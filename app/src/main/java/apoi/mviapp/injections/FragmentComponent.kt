package apoi.mviapp.injections

import apoi.mviapp.mobius.MainFragment
import dagger.Subcomponent

@FragmentScope
@Subcomponent(modules = [FragmentModule::class])
interface FragmentComponent {

    fun inject(fragment: MainFragment)
}
