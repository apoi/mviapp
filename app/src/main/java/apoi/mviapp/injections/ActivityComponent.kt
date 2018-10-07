package apoi.mviapp.injections

import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = [ActivityModule::class])
interface ActivityComponent {

    fun plusFragment(fragmentModule: FragmentModule): FragmentComponent
}
