package apoi.mviapp.mobius.domain

import com.spotify.mobius.Update

class MainLogic {

    fun createUpdate(): Update<MainModel, MainEvent, MainEffect> {
        return Update { model, event ->
            when (event) {
            }
        }
    }
}