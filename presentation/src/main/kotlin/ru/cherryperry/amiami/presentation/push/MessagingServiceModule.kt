package ru.cherryperry.amiami.presentation.push

import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap

@Module(subcomponents = [MessagingServiceSubcomponent::class])
abstract class MessagingServiceModule {

    @Binds
    @IntoMap
    @ClassKey(MessagingService::class)
    internal abstract fun bindFactory(builder: MessagingServiceSubcomponent.Builder): AndroidInjector.Factory<*>
}
