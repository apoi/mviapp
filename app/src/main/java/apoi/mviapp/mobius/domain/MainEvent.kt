package apoi.mviapp.mobius.domain

import apoi.mviapp.pojo.Photo

sealed class MainEvent

class LoadButtonClicked : MainEvent()
class PhotoClicked : MainEvent()
class ItemLoadSuccess(val photos: List<Photo>) : MainEvent()
class ItemLoadError(val error: String) : MainEvent()
