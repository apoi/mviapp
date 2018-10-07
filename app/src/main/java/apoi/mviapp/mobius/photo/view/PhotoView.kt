package apoi.mviapp.mobius.photo.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import apoi.mviapp.R
import apoi.mviapp.mobius.photo.domain.PhotoEvent
import apoi.mviapp.mobius.photo.domain.PhotoModel
import com.spotify.mobius.Connectable
import com.spotify.mobius.Connection
import com.spotify.mobius.functions.Consumer

class PhotoView(
    inflater: LayoutInflater,
    parent: ViewGroup?
) : Connectable<PhotoModel, PhotoEvent> {

    val view: View = inflater.inflate(R.layout.photo_fragment, parent, false)

    override fun connect(output: Consumer<PhotoEvent>): Connection<PhotoModel> {

        return object : Connection<PhotoModel> {
            override fun accept(value: PhotoModel) {
                render(value)
            }

            override fun dispose() {
            }
        }
    }

    private fun render(value: PhotoModel) {
    }
}
