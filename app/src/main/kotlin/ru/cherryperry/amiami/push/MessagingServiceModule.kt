package ru.cherryperry.amiami.push

import android.app.Service
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.android.ServiceKey
import dagger.multibindings.IntoMap

@Module(subcomponents = [MessagingServiceSubcomponent::class])
abstract class MessagingServiceModule {

    @Binds
    @IntoMap
    @ServiceKey(MessagingService::class)
    internal abstract fun bindFactory(builder: MessagingServiceSubcomponent.Builder): AndroidInjector.Factory<out Service>
}