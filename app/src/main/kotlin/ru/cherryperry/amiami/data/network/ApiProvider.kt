package ru.cherryperry.amiami.data.network

import android.text.TextUtils
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import retrofit2.Retrofit
import ru.cherryperry.amiami.AppPrefs
import ru.cherryperry.amiami.BuildConfig
import ru.cherryperry.amiami.R
import ru.cherryperry.amiami.data.network.github.GitHubApi
import ru.cherryperry.amiami.data.network.server.ServerApi
import rx.Emitter
import rx.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiProvider @Inject constructor(private val retrofitBuilder: Retrofit.Builder,
                                      private val appPrefs: AppPrefs) {

    private var api: ServerApi? = null

    fun api(): Observable<ServerApi> {
        if (api != null) return Observable.just(api)

        val remoteConfig = FirebaseRemoteConfig.getInstance()
        val configSettings = FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build()
        remoteConfig.setConfigSettings(configSettings)
        remoteConfig.setDefaults(R.xml.remote_config_defaults)
        return Observable.create<String>({ emitter ->
            remoteConfig.fetch()
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            remoteConfig.activateFetched()
                            val endpoint = remoteConfig.getString("server_base_url")
                            appPrefs.lastServer = endpoint
                            emitter.onNext(endpoint)
                            emitter.onCompleted()
                        } else {
                            emitter.onError(IllegalStateException("isSuccessful = false"))
                        }
                    }
                    .addOnFailureListener {
                        emitter.onError(it)
                    }
        }, Emitter.BackpressureMode.LATEST)
                .onErrorReturn {
                    if (TextUtils.isEmpty(appPrefs.lastServer)) throw it
                    appPrefs.lastServer
                }
                .map {
                    api = retrofitBuilder
                            .baseUrl(it)
                            .build()
                            .create(ServerApi::class.java)
                    api
                }
    }

    fun gitHubApi(): Observable<GitHubApi> = Observable.just(
            retrofitBuilder
                    .baseUrl(GitHubApi.URL)
                    .build()
                    .create(GitHubApi::class.java))
}