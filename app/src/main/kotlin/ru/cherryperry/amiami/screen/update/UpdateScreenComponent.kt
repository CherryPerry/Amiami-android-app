package ru.cherryperry.amiami.screen.update

import dagger.Component
import ru.cherryperry.amiami.ApplicationModule
import ru.cherryperry.amiami.network.APIModule
import javax.inject.Singleton

@Component(modules = arrayOf(
        ApplicationModule::class,
        APIModule::class
))
@Singleton
interface UpdateScreenComponent {
    fun inject(target: UpdatePresenter)
}