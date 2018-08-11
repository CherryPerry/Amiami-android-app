package ru.cherryperry.amiami.data.network

import android.content.Context
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import ru.cherryperry.amiami.data.BuildConfig
import ru.cherryperry.amiami.data.network.github.GitHubApi
import ru.cherryperry.amiami.data.network.server.ExchangeRatesGsonTypeAdapter
import ru.cherryperry.amiami.data.network.server.ServerApi
import ru.cherryperry.amiami.domain.model.ExchangeRates
import rx.schedulers.Schedulers
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class NetworkModule {

    companion object {
        private const val HTTP_CACHE_NAME = "http"
        private const val HTTP_CACHE_SIZE = 5 * 1024 * 1024L
        private const val TIMEOUT = 30L
    }

    @Singleton
    @Provides
    fun okHttpClient(context: Context): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.HEADERS
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
        val cacheDir = File(context.cacheDir, HTTP_CACHE_NAME)
        cacheDir.mkdirs()
        val cache = Cache(cacheDir, HTTP_CACHE_SIZE)
        return OkHttpClient.Builder()
            .addNetworkInterceptor(logging)
            .cache(cache)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun retrofitBuilder(client: OkHttpClient): Retrofit.Builder {
        val gson = GsonBuilder()
            .registerTypeAdapter(ExchangeRates::class.java, ExchangeRatesGsonTypeAdapter())
            .create()
        return Retrofit.Builder()
            .client(client)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
            .validateEagerly(true)
    }

    @Singleton
    @Provides
    fun gitHubApi(retrofitBuilder: Retrofit.Builder): GitHubApi = retrofitBuilder
        .baseUrl(GitHubApi.URL)
        .build()
        .create(GitHubApi::class.java)

    @Singleton
    @Provides
    fun serverApi(retrofitBuilder: Retrofit.Builder): ServerApi = retrofitBuilder
        .baseUrl(ServerApi.URL)
        .build()
        .create(ServerApi::class.java)
}
