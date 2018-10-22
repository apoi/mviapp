package apoi.mviapp.injections

import apoi.mviapp.photo.PhotoActivity
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = [ActivityModule::class])
interface ActivityComponent {

    fun plusFragment(fragmentModule: FragmentModule): FragmentComponent

    fun inject(activity: apoi.mviapp.mobius.view.ListActivity)

    fun inject(activity: apoi.mviapp.freesound.view.ListActivity)

    fun inject(activity: apoi.mviapp.mvi2.view.ListActivity)

    fun inject(activity: PhotoActivity)
}
