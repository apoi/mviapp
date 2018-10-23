package apoi.mviapp.common

import apoi.mviapp.pojo.Photo

sealed class ListEvent : Event {
    object Initial : ListEvent()
    object LoadButtonClicked : ListEvent()
    data class PhotoClicked(val photo: Photo) : ListEvent()
}
