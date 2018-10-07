package apoi.mviapp.mobius.photo.effecthandlers

import android.content.Context
import apoi.mviapp.mobius.photo.domain.PhotoEffect
import apoi.mviapp.mobius.photo.domain.PhotoEvent
import apoi.mviapp.network.Api
import com.spotify.mobius.rx2.RxMobius
import io.reactivex.ObservableTransformer
import javax.inject.Inject

class PhotoEffectHandlers @Inject constructor(
    private val context: Context,
    private val api: Api
) {

    fun createHandler(): ObservableTransformer<PhotoEffect, PhotoEvent> {
        return RxMobius.subtypeEffectHandler<PhotoEffect, PhotoEvent>()
            .build()
    }
}
