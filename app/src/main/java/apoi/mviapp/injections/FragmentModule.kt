package apoi.mviapp.injections

import android.content.Context
import androidx.fragment.app.Fragment
import dagger.Module
import dagger.Provides

@Module
class FragmentModule(private val fragment: Fragment) {

    @Provides
    @FragmentScope
    internal fun provideFragment(): Fragment {
        return fragment
    }

    @Provides
    @FragmentScope
    internal fun provideContext(): Context {
        return fragment.requireContext()
    }
}
