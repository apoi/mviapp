package apoi.mviapp.mobius.effecthandlers

import apoi.mviapp.mobius.domain.MainEffect
import apoi.mviapp.mobius.domain.MainEvent
import com.spotify.mobius.rx2.RxMobius
import io.reactivex.ObservableTransformer

class MainEffectHandlers {

    fun createHandler(): ObservableTransformer<MainEffect, MainEvent> {
        return RxMobius.subtypeEffectHandler<MainEffect, MainEvent>()
            .build()
    }
}