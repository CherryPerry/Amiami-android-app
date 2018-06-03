package ru.cherryperry.amiami

import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication

class AmiamiApplication : DaggerApplication() {

    override fun onCreate() {
        super.onCreate()
    }

    override fun applicationInjector(): AndroidInjector<DaggerApplication> {
        return DaggerApplicationComponent
                .builder()
                .applicationModule(ApplicationModule(this))
                .build() as AndroidInjector<DaggerApplication>
    }
}
