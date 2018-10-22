package apoi.mviapp.mvi2.domain

import apoi.mviapp.mvi2.arch.Event
import apoi.mviapp.pojo.Photo

sealed class ListEvent : Event {

    data class OnInitialized(val photos: List<Photo>) : ListEvent()
}