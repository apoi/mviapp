package apoi.mviapp.network

import apoi.mviapp.pojo.Photo
import io.reactivex.Single
import retrofit2.http.GET

interface Api {

    @GET("/photos")
    fun getPhotos(): Single<List<Photo>>
}
