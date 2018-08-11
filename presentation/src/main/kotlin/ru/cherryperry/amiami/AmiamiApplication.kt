package ru.cherryperry.amiami

import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import dagger.android.support.DaggerApplication
import io.fabric.sdk.android.Fabric

class AmiamiApplication : DaggerApplication() {

    private val injector by lazy {
        DaggerApplicationComponent
            .builder()
            .applicationModule(ApplicationModule(this))
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        val crashKit = Crashlytics.Builder()
            .core(CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build())
            .build()
        Fabric.with(this, crashKit)
    }

    override fun applicationInjector() = injector
}
