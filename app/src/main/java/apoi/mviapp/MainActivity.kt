package apoi.mviapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.main_activity.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.main_activity)

        button_mobius.setOnClickListener { launchActivity(apoi.mviapp.mobius.view.ListActivity::class.java) }
        button_freesound.setOnClickListener { launchActivity(apoi.mviapp.freesound.view.ListActivity::class.java) }
        button_mvi2.setOnClickListener { launchActivity(apoi.mviapp.mvi2.view.ListActivity::class.java) }
    }

    private fun launchActivity(activity: Class<*>) {
        startActivity(Intent(this, activity))
    }
}