package apoi.mviapp.mobius.list.domain

import apoi.mviapp.pojo.Photo

sealed class ListEffect

class LoadItems : ListEffect()
data class ShowError(val error: String) : ListEffect()
data class ShowPhoto(val photo: Photo) : ListEffect()
