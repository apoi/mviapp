package apoi.mviapp.freesound.list.view

import apoi.mviapp.network.Api
import apoi.mviapp.pojo.Photo
import io.reactivex.Single

class ListLoadInteractor(private val api: Api) {

    fun loadList(): Single<List<Photo>> {
        return api.getPhotos()
    }
}
