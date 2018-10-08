package apoi.mviapp.pojo

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Photo(
    val albumId: String,
    val id: Int,
    val title: String,
    val url: String,
    val thumbnailUrl: String
) : Parcelable
