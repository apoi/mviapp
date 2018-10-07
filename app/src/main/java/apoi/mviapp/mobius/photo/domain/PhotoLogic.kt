package apoi.mviapp.mobius.photo.domain

import com.spotify.mobius.Update

class PhotoLogic {

    fun createUpdate(): Update<PhotoModel, PhotoEvent, PhotoEffect> {
        return Update { model, event ->
            when (event) {
            }
        }
    }
}
