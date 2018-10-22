package apoi.mviapp.mobius.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import apoi.mviapp.core.BaseFragment
import apoi.mviapp.mobius.domain.ListEffect
import apoi.mviapp.mobius.domain.ListEvent
import apoi.mviapp.mobius.domain.ListLogic
import apoi.mviapp.mobius.domain.ListModel
import apoi.mviapp.mobius.domain.ListEffectHandlers
import com.spotify.mobius.MobiusLoop
import com.spotify.mobius.Update
import com.spotify.mobius.android.AndroidLogger
import com.spotify.mobius.android.MobiusAndroid
import com.spotify.mobius.rx2.RxMobius
import io.reactivex.ObservableTransformer
import javax.inject.Inject

class ListFragment : BaseFragment() {

    @Inject
    internal lateinit var listEffectHandlers: ListEffectHandlers

    private val listLogic = ListLogic()

    private lateinit var listView: ListView

    private lateinit var controller: MobiusLoop.Controller<ListModel, ListEvent>

    override fun inject() {
        getComponent().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return ListView(inflater, container).also {
            listView = it
        }.view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        controller = MobiusAndroid.controller(
            createLoop(listLogic.createUpdate(), listEffectHandlers.createHandler()),
            ListModel()
        )

        controller.connect(listView)
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
        update: Update<ListModel, ListEvent, ListEffect>,
        effectHandler: ObservableTransformer<ListEffect, ListEvent>
    ): MobiusLoop.Factory<ListModel, ListEvent, ListEffect> {
        return RxMobius.loop(update, effectHandler)
            .logger(AndroidLogger.tag("List"))
    }
}
