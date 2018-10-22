package apoi.mviapp.freesound.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import apoi.mviapp.R
import apoi.mviapp.pojo.Photo

class PhotoAdapter(
    private val clickListener: (Photo) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val photos = ArrayList<Photo>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PhotoViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as PhotoViewHolder).apply {
            val photo = photos[position]
            setPhoto(photo)
            setClickListener(View.OnClickListener { clickListener.invoke(photo) })
        }
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        (holder as PhotoViewHolder).setClickListener(null)
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
