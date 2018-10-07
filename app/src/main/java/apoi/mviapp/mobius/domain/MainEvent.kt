package apoi.mviapp.mobius.domain

sealed class MainEvent

class LoadButtonClicked : MainEvent()
class PhotoClicked : MainEvent()
class ItemLoadSuccess : MainEvent()
class ItemLoadError(error: String) : MainEvent()
