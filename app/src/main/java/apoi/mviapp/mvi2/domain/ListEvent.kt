package apoi.mviapp.mvi2.domain

import apoi.mviapp.mvi2.arch.Event
import apoi.mviapp.pojo.Photo

sealed class ListEvent : Event {
    object OnInitialized : ListEvent()
    object LoadButtonClicked : ListEvent()
    data class PhotoClicked(val photo: Photo) : ListEvent()
}
