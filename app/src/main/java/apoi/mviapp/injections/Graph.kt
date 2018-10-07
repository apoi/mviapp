package apoi.mviapp.injections

import apoi.mviapp.MviApp
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class])
interface Graph {

    fun plusActivity(activityModule: ActivityModule): ActivityComponent

    fun inject(application: MviApp)
}
