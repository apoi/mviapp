package apoi.mviapp.freesound.list.view.domain

import apoi.mviapp.freesound.arch.Reducer

val reducer: Reducer<ListUiResult, ListUiModel> =
    { current: ListUiModel, result: ListUiResult -> current.reduceModel(result) }

private fun ListUiModel.reduceModel(result: ListUiResult): ListUiModel =
    when (result) {
        is ListUiResult.NoChange -> this
        is ListUiResult.ItemLoadSuccess -> this
        is ListUiResult.ItemLoadError -> this
    }
