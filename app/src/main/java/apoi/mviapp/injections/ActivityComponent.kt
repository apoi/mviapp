package apoi.mviapp.injections

import apoi.mviapp.mobius.list.view.ListActivity
import apoi.mviapp.mobius.photo.view.PhotoActivity
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = [ActivityModule::class])
interface ActivityComponent {

    fun plusFragment(fragmentModule: FragmentModule): FragmentComponent

    fun inject(activity: ListActivity)

    fun inject(activity: PhotoActivity)
}
