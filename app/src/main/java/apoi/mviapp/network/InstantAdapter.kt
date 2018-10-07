package apoi.mviapp.network

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import org.threeten.bp.Instant

class InstantAdapter {

    @ToJson
    fun toJson(instant: Instant): String {
        return instant.toString()
    }

    @FromJson
    fun fromJson(value: String): Instant {
        return Instant.parse(value)
    }
}
