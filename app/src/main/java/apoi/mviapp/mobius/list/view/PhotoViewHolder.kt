package apoi.mviapp.mobius.list.view

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import apoi.mviapp.R
import apoi.mviapp.pojo.Photo
import com.squareup.picasso.Picasso

class PhotoViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val photoView = view.findViewById<ImageView>(R.id.item_photo)
    private val title = view.findViewById<TextView>(R.id.item_title)
    private val description = view.findViewById<TextView>(R.id.item_description)

    fun setPhoto(photo: Photo) {
        Picasso.get()
            .load(photo.thumbnailUrl)
            .fit()
            .centerCrop()
            .into(photoView)

        title.text = "Photo id: ${photo.id}"
        description.text = photo.title.capitalize()
    }
}
