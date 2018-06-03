package ru.cherryperry.amiami

import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import dagger.android.support.AndroidSupportInjectionModule
import ru.cherryperry.amiami.network.APIModule
import ru.cherryperry.amiami.push.MessagingServiceModule
import ru.cherryperry.amiami.screen.base.AppViewModelFactoryModule
import ru.cherryperry.amiami.screen.highlight.HighlightActivityModule
import ru.cherryperry.amiami.screen.main.MainActivityModule
import ru.cherryperry.amiami.screen.main.SettingsFragmentModule
import ru.cherryperry.amiami.screen.update.UpdateDialogModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    // base
    AndroidInjectionModule::class,
    AndroidSupportInjectionModule::class,
    AppViewModelFactoryModule::class,
    // data
    ApplicationModule::class,
    APIModule::class,
    // presentation
    MainActivityModule::class,
    HighlightActivityModule::class,
    MessagingServiceModule::class,
    SettingsFragmentModule::class,
    UpdateDialogModule::class
])
interface ApplicationComponent : AndroidInjector<DaggerApplication> {

    @Component.Builder
    interface Builder {

        fun applicationModule(applicationModule: ApplicationModule): Builder

        fun build(): ApplicationComponent
    }
}
