package apoi.mviapp.common

import android.os.Parcelable
import apoi.mviapp.pojo.Photo
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ListState(
    val inProgress: Boolean = false,
    val photos: List<Photo> = emptyList()
) : State, Parcelable
