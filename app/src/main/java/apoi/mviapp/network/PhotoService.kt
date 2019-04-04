package apoi.mviapp.network

import java.util.concurrent.TimeUnit

class PhotoService(private val api: Api) {

    fun getPhotos() = api.getPhotos()
        .delay(3000, TimeUnit.MILLISECONDS)
}
