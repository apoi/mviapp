package apoi.mviapp.mobius.domain

import apoi.mviapp.pojo.Photo

data class MainModel(
    val inProgress: Boolean = false,
    val itemList: List<Photo> = emptyList(),
    val error: String = ""
)
