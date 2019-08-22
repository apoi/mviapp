package apoi.mviapp.mobius.domain

import apoi.mviapp.common.ListState
import com.spotify.mobius.Effects
import com.spotify.mobius.Next
import com.spotify.mobius.Update

class ListLogic {

    fun createUpdate(): Update<ListState, ListEvent, ListEffect> {
        return Update { model, event ->
            when (event) {
                is LoadButtonClicked, is PullToRefresh -> {
                    Next.next(
                        model.copy(inProgress = true),
                        Effects.effects(
                            LoadItems()
                        )
                    )
                }
                is ItemLoadSuccess -> {
                    Next.next(
                        model.copy(inProgress = false, photos = event.photos)
                    )
                }
                is ItemLoadError -> {
                    Next.next(
                        model.copy(inProgress = false),
                        Effects.effects(
                            ShowError(event.error)
                        )
                    )
                }
                is PhotoClicked -> {
                    Next.next(
                        model,
                        Effects.effects(ShowPhoto(event.photo))
                    )
                }
            }
        }
    }
}
