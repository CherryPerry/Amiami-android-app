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
import ru.cherryperry.amiami.BuildConfig
import ru.cherryperry.amiami.data.network.server.ExchangeRate
import ru.cherryperry.amiami.data.network.server.ExchangeRateGsonTypeAdapter
import rx.schedulers.Schedulers
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module(includes = [ApiModule::class])
class NetworkModule {

    @Singleton
    @Provides
    fun okHttpClient(context: Context): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.HEADERS else HttpLoggingInterceptor.Level.NONE
        val cacheDir = File(context.cacheDir, "http")
        cacheDir.mkdirs()
        val cache = Cache(cacheDir, 5 * 1024 * 1024)
        return OkHttpClient.Builder()
            .addNetworkInterceptor(logging)
            .cache(cache)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun retrofitBuilder(client: OkHttpClient): Retrofit.Builder {
        val gson = GsonBuilder()
            .registerTypeAdapter(ExchangeRate::class.java, ExchangeRateGsonTypeAdapter())
            .create()
        return Retrofit.Builder()
            .client(client)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
            .validateEagerly(true)
    }
}