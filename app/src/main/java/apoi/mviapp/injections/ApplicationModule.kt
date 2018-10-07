package apoi.mviapp.injections

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import apoi.mviapp.API_ENDPOINT
import apoi.mviapp.SHARED_PREFS
import apoi.mviapp.network.Api
import apoi.mviapp.network.InstantAdapter
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber
import javax.inject.Singleton

@Module
class ApplicationModule(private val application: Application) {

    @Provides
    @Singleton
    @ForApplication
    internal fun provideApplicationContext(): Context {
        return application.applicationContext
    }

    @Provides
    @Singleton
    internal fun provideApplication(): Application {
        return application
    }

    @Provides
    @Singleton
    internal fun provideLoggingTree(): Timber.Tree {
        return Timber.DebugTree()
    }

    @Provides
    @Singleton
    internal fun provideSharedPreferences(@ForApplication context: Context): SharedPreferences {
        return context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    internal fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .build()
    }

    @Provides
    @Singleton
    internal fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(InstantAdapter())
            .build()
    }

    @Provides
    @Singleton
    internal fun provideApi(client: OkHttpClient, moshi: Moshi): Api {
        val retrofit = Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(API_ENDPOINT)
            .client(client)
            .build()

        return retrofit.create(Api::class.java)
    }
}
