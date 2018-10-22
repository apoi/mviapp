package apoi.mviapp.injections

import dagger.Subcomponent

@FragmentScope
@Subcomponent(modules = [FragmentModule::class])
interface FragmentComponent {

    fun inject(fragment: apoi.mviapp.mobius.list.view.ListFragment)

    fun inject(fragment: apoi.mviapp.freesound.list.view.ListFragment)

    fun inject(fragment: apoi.mviapp.mvi2.view.ListFragment)
}
