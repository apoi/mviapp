package apoi.mviapp.mobius.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import apoi.mviapp.R
import apoi.mviapp.extensions.setVisibility
import apoi.mviapp.mobius.domain.LoadButtonClicked
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

    private val progressBar = view.findViewById<ProgressBar>(R.id.progress_bar)
    private val loadButton = view.findViewById<Button>(R.id.load_button)
    private val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)

    override fun connect(output: Consumer<MainEvent>): Connection<MainModel> {

        setListeners(output)

        return object : Connection<MainModel> {
            override fun accept(value: MainModel) {
                render(value)
            }

            override fun dispose() {
                clearListeners()
            }
        }
    }

    private fun setListeners(output: Consumer<MainEvent>) {
        loadButton.setOnClickListener { output.accept(LoadButtonClicked()) }
    }

    private fun clearListeners() {
        loadButton.setOnClickListener(null)
    }

    private fun render(value: MainModel) {
        progressBar.setVisibility(value.inProgress)
        loadButton.setVisibility(!value.inProgress)
    }
}
