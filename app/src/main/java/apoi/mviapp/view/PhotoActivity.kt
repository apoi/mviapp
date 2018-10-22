package apoi.mviapp.view

import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import apoi.mviapp.R
import apoi.mviapp.core.BaseActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.photo_activity.*

const val PHOTO = "photo"

class PhotoActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        getComponent().inject(this)

        setContentView(R.layout.photo_activity)
        initImageView(intent.getStringExtra(PHOTO) ?: "")
    }

    override fun inject() {
        getComponent().inject(this)
    }

    private fun initImageView(source: String) {
        Picasso.get()
            .load(source)
            .centerInside()
            .fit()
            .into(photo_view)
    }
}
