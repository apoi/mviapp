package apoi.mviapp.mobius.list.effecthandlers

import android.content.Context
import android.widget.Toast
import apoi.mviapp.mobius.list.domain.ItemLoadError
import apoi.mviapp.mobius.list.domain.ItemLoadSuccess
import apoi.mviapp.mobius.list.domain.ListEffect
import apoi.mviapp.mobius.list.domain.ListEvent
import apoi.mviapp.mobius.list.domain.LoadItems
import apoi.mviapp.mobius.list.domain.ShowError
import apoi.mviapp.network.Api
import apoi.mviapp.network.ErrorMapper
import com.spotify.mobius.rx2.RxMobius
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ListEffectHandlers @Inject constructor(
    private val context: Context,
    private val api: Api
) {

    fun createHandler(): ObservableTransformer<ListEffect, ListEvent> {
        return RxMobius.subtypeEffectHandler<ListEffect, ListEvent>()
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

    private fun handleLoadRequest(effects: Observable<LoadItems>): Observable<ListEvent> {
        return effects
            .subscribeOn(Schedulers.io())
            .flatMap {
                api.getPhotos()
                    .toObservable()
                    .map<ListEvent> { ItemLoadSuccess(it) }
                    .onErrorReturn {
                        ItemLoadError(ErrorMapper(context, it).errorToMessage())
                    }
            }
    }
}
