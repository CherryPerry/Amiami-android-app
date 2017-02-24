package ru.cherryperry.amiami.screen.main

import dagger.Component
import ru.cherryperry.amiami.ApplicationModule
import ru.cherryperry.amiami.network.APIModule
import javax.inject.Singleton

@Component(modules = arrayOf(
        ApplicationModule::class,
        APIModule::class
))
@Singleton
interface MainScreenComponent {
    fun inject(mainPresenter: MainPresenter)

    fun inject(mainActivity: MainActivity)
}