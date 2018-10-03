package ru.cherryperry.amiami

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import ru.cherryperry.amiami.domain.di.ApplicationVersion
import ru.cherryperry.amiami.domain.performance.PerformanceTracer
import ru.cherryperry.amiami.presentation.util.FirebasePerformanceTracer

@Module
class ApplicationModule(private val app: Application) {

    @Provides
    fun applicationContext(): Context = app

    @Provides
    fun application(): Application = app

    @Provides
    @ApplicationVersion
    fun applicationVersion(): String = BuildConfig.VERSION_NAME

    @Provides
    fun performanceTracer(): PerformanceTracer = FirebasePerformanceTracer()
}
