package apoi.mviapp.mobius.domain

import apoi.mviapp.pojo.Photo

sealed class MainEffect

class LoadItems : MainEffect()
data class ShowError(val error: String) : MainEffect()
data class ShowPhoto(val photo: Photo) : MainEffect()
