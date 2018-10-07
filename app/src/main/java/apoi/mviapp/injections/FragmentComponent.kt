package apoi.mviapp.injections

import apoi.mviapp.mobius.list.view.ListFragment
import apoi.mviapp.mobius.photo.view.PhotoFragment
import dagger.Subcomponent

@FragmentScope
@Subcomponent(modules = [FragmentModule::class])
interface FragmentComponent {

    fun inject(fragment: ListFragment)

    fun inject(fragment: PhotoFragment)
}
