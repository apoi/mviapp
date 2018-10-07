package apoi.mviapp.network

import android.content.Context
import apoi.mviapp.R
import retrofit2.HttpException
import timber.log.Timber
import java.net.SocketException
import java.net.SocketTimeoutException

open class ErrorMapper(private val context: Context, private val cause: Throwable?) {

    fun errorToMessage(): String {
        Timber.e(cause)

        return if (cause is HttpException) {
            context.getString(R.string.server_error)
        } else if (isNetworkException(cause)) {
            context.getString(R.string.network_error)
        } else {
            context.getString(R.string.unknown_error)
        }
    }

    private fun isNetworkException(throwable: Throwable?): Boolean {
        return throwable is SocketException ||
            throwable is SocketTimeoutException
    }
}
