package apoi.mviapp.injections

import apoi.mviapp.mobius.photo.view.PhotoActivity
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = [ActivityModule::class])
interface ActivityComponent {

    fun plusFragment(fragmentModule: FragmentModule): FragmentComponent

    fun inject(activity: apoi.mviapp.mobius.list.view.ListActivity)

    fun inject(activity: apoi.mviapp.freesound.list.view.ListActivity)

    fun inject(activity: apoi.mviapp.mvi2.view.ListActivity)

    fun inject(activity: PhotoActivity)
}
