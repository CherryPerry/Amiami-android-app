package ru.cherryperry.amiami

import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication

class AmiamiApplication : DaggerApplication() {

    @Suppress("UNCHECKED_CAST")
    private val injector by lazy {
        DaggerApplicationComponent
                .builder()
                .applicationModule(ApplicationModule(this))
                .build() as AndroidInjector<DaggerApplication>
    }

    override fun applicationInjector(): AndroidInjector<DaggerApplication> {
        return injector
    }
}
