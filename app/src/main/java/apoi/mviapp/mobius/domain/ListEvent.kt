package apoi.mviapp.mobius.domain

import apoi.mviapp.pojo.Photo

sealed class ListEvent

class LoadButtonClicked : ListEvent()
class PullToRefresh : ListEvent()
class PhotoClicked(val photo: Photo) : ListEvent()
class ItemLoadSuccess(val photos: List<Photo>) : ListEvent()
class ItemLoadError(val error: String) : ListEvent()
