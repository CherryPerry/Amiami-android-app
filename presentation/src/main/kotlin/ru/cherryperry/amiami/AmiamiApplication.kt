package ru.cherryperry.amiami

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDex
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import dagger.android.support.DaggerApplication
import io.fabric.sdk.android.Fabric
import io.reactivex.plugins.RxJavaPlugins

class AmiamiApplication : DaggerApplication() {

    private val injector by lazy {
        DaggerApplicationComponent
            .builder()
            .applicationModule(ApplicationModule(this))
            .build()
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        val crashKit = Crashlytics.Builder()
            .core(CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build())
            .build()
        Fabric.with(this, crashKit)
        RxJavaPlugins.setErrorHandler {
            // TODO Log it
        }
    }

    override fun applicationInjector() = injector
}
