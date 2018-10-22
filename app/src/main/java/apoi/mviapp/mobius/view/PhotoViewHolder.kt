package apoi.mviapp.mobius.view

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import apoi.mviapp.R
import apoi.mviapp.pojo.Photo
import com.squareup.picasso.Picasso

class PhotoViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val container = view.findViewById<ViewGroup>(R.id.item_container)
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

    fun setClickListener(listener: View.OnClickListener?) {
        container.setOnClickListener(listener)
    }
}
