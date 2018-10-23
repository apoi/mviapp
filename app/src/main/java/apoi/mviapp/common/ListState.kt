package apoi.mviapp.common

import apoi.mviapp.pojo.Photo

data class ListState(
    val inProgress: Boolean = false,
    val photos: List<Photo> = emptyList()
) : State
