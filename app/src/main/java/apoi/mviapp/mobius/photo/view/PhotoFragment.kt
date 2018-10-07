package apoi.mviapp.mobius.photo.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import apoi.mviapp.core.BaseFragment
import apoi.mviapp.mobius.photo.domain.PhotoEvent
import apoi.mviapp.mobius.photo.domain.PhotoLogic
import apoi.mviapp.mobius.photo.domain.PhotoModel
import apoi.mviapp.mobius.photo.effecthandlers.PhotoEffectHandlers
import com.spotify.mobius.MobiusLoop
import com.spotify.mobius.android.AndroidLogger
import com.spotify.mobius.android.MobiusAndroid
import com.spotify.mobius.rx2.RxMobius
import javax.inject.Inject

class PhotoFragment : BaseFragment() {

    @Inject
    internal lateinit var photoEffectHandlers: PhotoEffectHandlers

    private val listLogic = PhotoLogic()

    private lateinit var photoView: PhotoView

    private lateinit var controller: MobiusLoop.Controller<PhotoModel, PhotoEvent>

    override fun inject() {
        getComponent().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return PhotoView(inflater, container).also {
            photoView = it
        }.view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        controller = MobiusAndroid.controller(
            RxMobius.loop(listLogic.createUpdate(), photoEffectHandlers.createHandler())
                .logger(AndroidLogger.tag("Photo")),
            PhotoModel()
        )

        controller.connect(photoView)
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
}
