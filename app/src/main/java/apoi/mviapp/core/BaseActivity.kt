package apoi.mviapp.core

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import apoi.mviapp.MviApp
import apoi.mviapp.injections.ActivityComponent
import apoi.mviapp.injections.ActivityModule

abstract class BaseActivity : AppCompatActivity() {

    private var component: ActivityComponent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        inject()
    }

    fun getComponent(): ActivityComponent {
        if (component == null) {
            component = (application as MviApp).graph()
                .plusActivity(ActivityModule(this))
        }

        return component!!
    }

    protected abstract fun inject()
}
