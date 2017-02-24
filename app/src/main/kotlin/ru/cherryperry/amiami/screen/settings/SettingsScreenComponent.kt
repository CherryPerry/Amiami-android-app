package ru.cherryperry.amiami.screen.settings

import dagger.Component
import ru.cherryperry.amiami.ApplicationModule
import ru.cherryperry.amiami.network.APIModule
import javax.inject.Singleton

@Component(modules = arrayOf(
        ApplicationModule::class,
        APIModule::class
))
@Singleton
interface SettingsScreenComponent {
    fun inject(settingsFragment: SettingsFragment)
}