package apoi.mviapp.mobius.domain

import android.content.Context
import android.content.Intent
import android.widget.Toast
import apoi.mviapp.view.PHOTO
import apoi.mviapp.view.PhotoActivity
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
            .addTransformer(LoadItems::class.java, this::handleLoadItems)
            .addConsumer(ShowPhoto::class.java, this::handleShowPhoto, AndroidSchedulers.mainThread())
            .addConsumer(ShowError::class.java, this::handleShowError, AndroidSchedulers.mainThread())
            .build()
    }

    private fun handleShowPhoto(effect: ShowPhoto) {
        context.startActivity(Intent(context, PhotoActivity::class.java).apply {
            putExtra(PHOTO, effect.photo.url)
        })
    }

    private fun handleShowError(effect: ShowError) {
        Toast.makeText(
            context,
            effect.error,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun handleLoadItems(effects: Observable<LoadItems>): Observable<ListEvent> {
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
