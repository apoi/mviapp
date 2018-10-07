package apoi.mviapp.mobius.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import apoi.mviapp.R
import apoi.mviapp.mobius.domain.MainEvent
import apoi.mviapp.mobius.domain.MainModel
import com.spotify.mobius.Connectable
import com.spotify.mobius.Connection
import com.spotify.mobius.functions.Consumer

class MainView(
    inflater: LayoutInflater,
    parent: ViewGroup?
) : Connectable<MainModel, MainEvent> {

    val view: View = inflater.inflate(R.layout.main_fragment, parent, false)

    override fun connect(output: Consumer<MainEvent>): Connection<MainModel> {

        return object : Connection<MainModel> {
            override fun accept(value: MainModel) {
                render(value)
            }

            override fun dispose() {
            }
        }
    }

    private fun render(value: MainModel) {
    }
}
