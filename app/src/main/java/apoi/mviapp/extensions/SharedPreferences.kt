package apoi.mviapp.extensions

import android.content.SharedPreferences

fun SharedPreferences.getString(key: String): String {
    return this.getString(key, "")!!
}
