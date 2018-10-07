package apoi.mviapp.mobius.effecthandlers

import android.content.Context
import android.widget.Toast
import apoi.mviapp.mobius.domain.ItemLoadError
import apoi.mviapp.mobius.domain.ItemLoadSuccess
import apoi.mviapp.mobius.domain.LoadItems
import apoi.mviapp.mobius.domain.MainEffect
import apoi.mviapp.mobius.domain.MainEvent
import apoi.mviapp.mobius.domain.ShowError
import apoi.mviapp.network.Api
import apoi.mviapp.network.ErrorMapper
import com.spotify.mobius.rx2.RxMobius
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MainEffectHandlers @Inject constructor(
    private val context: Context,
    private val api: Api
) {

    fun createHandler(): ObservableTransformer<MainEffect, MainEvent> {
        return RxMobius.subtypeEffectHandler<MainEffect, MainEvent>()
            .addTransformer(LoadItems::class.java, this::handleLoadRequest)
            .addConsumer(ShowError::class.java, this::handleError, AndroidSchedulers.mainThread())
            .build()
    }

    private fun handleError(error: ShowError) {
        Toast.makeText(
            context,
            error.error,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun handleLoadRequest(effects: Observable<LoadItems>): Observable<MainEvent> {
        return effects
            .subscribeOn(Schedulers.io())
            .flatMap {
                api.getPhotos()
                    .toObservable()
                    .map<MainEvent> { ItemLoadSuccess(it) }
                    .onErrorReturn {
                        ItemLoadError(ErrorMapper(context, it).errorToMessage())
                    }
            }
    }
}
