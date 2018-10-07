package apoi.mviapp.extensions

import android.view.View

fun View.setVisibility(visible: Boolean) {
    this.visibility = if (visible) View.VISIBLE else View.GONE
}
