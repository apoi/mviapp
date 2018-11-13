package apoi.mviapp.injections

import apoi.mviapp.MainActivity
import apoi.mviapp.photo.PhotoActivity
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = [ActivityModule::class])
interface ActivityComponent {

    fun plusFragment(fragmentModule: FragmentModule): FragmentComponent

    fun inject(activity: MainActivity)

    fun inject(activity: PhotoActivity)
}
