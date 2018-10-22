package apoi.mviapp.mobius.domain

import apoi.mviapp.pojo.Photo

data class ListModel(
    val inProgress: Boolean = false,
    val photos: List<Photo> = emptyList()
)
