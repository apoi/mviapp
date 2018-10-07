package apoi.mviapp.injections

import androidx.appcompat.app.AppCompatActivity
import dagger.Module
import dagger.Provides

@Module
class ActivityModule(private val activity: AppCompatActivity) {

    @Provides
    @ActivityScope
    internal fun provideActivity(): AppCompatActivity {
        return activity
    }
}
