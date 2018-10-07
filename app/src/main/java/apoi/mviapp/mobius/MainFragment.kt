package apoi.mviapp.mobius

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import apoi.mviapp.core.BaseFragment
import apoi.mviapp.mobius.domain.MainEffect
import apoi.mviapp.mobius.domain.MainEvent
import apoi.mviapp.mobius.domain.MainLogic
import apoi.mviapp.mobius.domain.MainModel
import apoi.mviapp.mobius.effecthandlers.MainEffectHandlers
import apoi.mviapp.mobius.view.MainView
import com.spotify.mobius.MobiusLoop
import com.spotify.mobius.Update
import com.spotify.mobius.android.AndroidLogger
import com.spotify.mobius.android.MobiusAndroid
import com.spotify.mobius.rx2.RxMobius
import io.reactivex.ObservableTransformer
import javax.inject.Inject

class MainFragment : BaseFragment() {

    @Inject
    internal lateinit var mainEffectHandlers: MainEffectHandlers

    private val mainLogic = MainLogic()

    private lateinit var mainView: MainView

    private lateinit var controller: MobiusLoop.Controller<MainModel, MainEvent>

    override fun inject() {
        getComponent().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return MainView(inflater, container).also {
            mainView = it
        }.view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        controller = MobiusAndroid.controller(
            createLoop(mainLogic.createUpdate(), mainEffectHandlers.createHandler()),
            MainModel()
        )

        controller.connect(mainView)
    }

    override fun onResume() {
        super.onResume()
        controller.start()
    }

    override fun onPause() {
        controller.stop()
        super.onPause()
    }

    override fun onDestroyView() {
        controller.disconnect()
        super.onDestroyView()
    }

    private fun createLoop(
        update: Update<MainModel, MainEvent, MainEffect>,
        effectHandler: ObservableTransformer<MainEffect, MainEvent>
    ): MobiusLoop.Factory<MainModel, MainEvent, MainEffect> {
        return RxMobius.loop(update, effectHandler)
            .logger(AndroidLogger.tag("Main"))
    }
}
