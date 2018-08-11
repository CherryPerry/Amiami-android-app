package ru.cherryperry.amiami

import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import ru.cherryperry.amiami.data.DataModule
import ru.cherryperry.amiami.presentation.activity.SingleActivityModule
import ru.cherryperry.amiami.presentation.base.AppViewModelFactoryModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    // base
    AndroidInjectionModule::class,
    AndroidSupportInjectionModule::class,
    AppViewModelFactoryModule::class,
    // data
    ApplicationModule::class,
    DataModule::class,
    // presentation
    SingleActivityModule::class
])
interface ApplicationComponent : AndroidInjector<AmiamiApplication> {

    @Component.Builder
    interface Builder {

        fun applicationModule(applicationModule: ApplicationModule): Builder

        fun build(): ApplicationComponent
    }
}
