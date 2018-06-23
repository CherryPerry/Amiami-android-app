package ru.cherryperry.amiami

import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import dagger.android.support.AndroidSupportInjectionModule
import ru.cherryperry.amiami.data.network.NetworkModule
import ru.cherryperry.amiami.screen.activity.SingleActivityModule
import ru.cherryperry.amiami.screen.base.AppViewModelFactoryModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    // base
    AndroidInjectionModule::class,
    AndroidSupportInjectionModule::class,
    AppViewModelFactoryModule::class,
    // data
    ApplicationModule::class,
    NetworkModule::class,
    // presentation
    SingleActivityModule::class
])
interface ApplicationComponent : AndroidInjector<DaggerApplication> {

    @Component.Builder
    interface Builder {

        fun applicationModule(applicationModule: ApplicationModule): Builder

        fun build(): ApplicationComponent
    }
}
