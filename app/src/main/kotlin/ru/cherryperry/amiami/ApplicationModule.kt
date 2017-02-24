package ru.cherryperry.amiami

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule(val app: Application) {
    @Provides
    @Singleton
    fun applicationContext(): Context = app

    @Provides
    @Singleton
    fun application(): Application = app
}