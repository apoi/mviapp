package apoi.mviapp.network

import apoi.mviapp.pojo.Photo
import io.reactivex.Single
import java.lang.IllegalStateException
import javax.inject.Inject

class PhotoService @Inject constructor(
    private val api: Api
) {

    fun getPhotos(): Single<List<Photo>> {
        return Single.error<List<Photo>>(IllegalStateException("ERR"))
    }
}
