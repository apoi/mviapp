package apoi.mviapp.freesound.domain

import apoi.mviapp.freesound.arch.Reducer

val reducer: Reducer<ListResult, ListState> =
    { current: ListState, result: ListResult -> current.reduceModel(result) }

private fun ListState.reduceModel(result: ListResult): ListState =
    when (result) {
        is ListResult.NoChange -> this
        is ListResult.ItemLoadSuccess -> this.copy(photos = result.photos)
        is ListResult.ItemLoadError -> this
    }
