package apoi.mviapp.mobius.list.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import apoi.mviapp.R
import apoi.mviapp.pojo.Photo

class PhotoAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val photos = ArrayList<Photo>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PhotoViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as PhotoViewHolder).setPhoto(photos[position])
    }

    fun setPhotos(value: List<Photo>) {
        if (value != photos) {
            photos.clear()
            photos.addAll(value)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return photos.size
    }
}
