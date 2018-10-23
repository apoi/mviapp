package apoi.mviapp.mvi2.domain

import apoi.mviapp.mvi2.arch.State
import apoi.mviapp.pojo.Photo

data class ListState(
    val inProgress: Boolean = false,
    val photos: List<Photo> = emptyList()
) : State
