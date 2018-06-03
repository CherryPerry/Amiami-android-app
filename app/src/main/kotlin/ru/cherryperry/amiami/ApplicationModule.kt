package ru.cherryperry.amiami

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class ApplicationModule(private val app: Application) {

    @Provides
    fun applicationContext(): Context = app

    @Provides
    fun application(): Application = app
}