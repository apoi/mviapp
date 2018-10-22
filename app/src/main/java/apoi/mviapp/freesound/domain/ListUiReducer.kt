package apoi.mviapp.freesound.domain

import apoi.mviapp.freesound.arch.Reducer

val reducer: Reducer<ListUiResult, ListUiModel> =
    { current: ListUiModel, result: ListUiResult -> current.reduceModel(result) }

private fun ListUiModel.reduceModel(result: ListUiResult): ListUiModel =
    when (result) {
        is ListUiResult.NoChange -> this
        is ListUiResult.ItemLoadSuccess -> this.copy(photos = result.photos)
        is ListUiResult.ItemLoadError -> this
    }
