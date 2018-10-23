package apoi.mviapp

import android.app.Application
import apoi.mviapp.injections.ApplicationModule
import apoi.mviapp.injections.DaggerGraph
import apoi.mviapp.injections.Graph
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import okhttp3.OkHttpClient
import timber.log.Timber
import javax.inject.Inject

class MviApp : Application() {

    @Inject
    internal lateinit var loggingTree: Timber.Tree

    @Inject
    internal lateinit var okhttp: OkHttpClient

    private lateinit var graph: Graph

    override fun onCreate() {
        super.onCreate()

        inject()
        initLogging()
        initPicasso()
    }

    private fun inject() {
        graph = DaggerGraph.builder()
            .applicationModule(ApplicationModule(this))
            .build()
            .also { it.inject(this) }
    }

    fun graph(): Graph {
        return graph
    }

    private fun initLogging() {
        Timber.uprootAll()
        Timber.plant(loggingTree)
    }

    private fun initPicasso() {
        val picasso = Picasso.Builder(applicationContext)
            .indicatorsEnabled(BuildConfig.DEBUG)
            .loggingEnabled(BuildConfig.DEBUG)
            .downloader(OkHttp3Downloader(okhttp))
            .build()

        Picasso.setSingletonInstance(picasso)
    }
}
