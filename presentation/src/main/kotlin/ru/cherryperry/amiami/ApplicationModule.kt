package ru.cherryperry.amiami

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import ru.cherryperry.amiami.domain.di.ApplicationVersion

@Module
class ApplicationModule(private val app: Application) {

    @Provides
    fun applicationContext(): Context = app

    @Provides
    fun application(): Application = app

    @Provides
    @ApplicationVersion
    fun applicationVersion(): String = BuildConfig.VERSION_NAME
}
