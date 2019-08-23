package apoi.mviapp.common

import android.os.Parcelable
import apoi.mviapp.pojo.Photo
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ListState(
    val photos: List<Photo> = emptyList(),
    val inProgress: Boolean = false,
    val error: String? = null
) : State, Parcelable
