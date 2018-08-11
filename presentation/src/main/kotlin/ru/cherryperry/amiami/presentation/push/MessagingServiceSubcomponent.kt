package ru.cherryperry.amiami.presentation.push

import dagger.Subcomponent
import dagger.android.AndroidInjector

@Subcomponent
interface MessagingServiceSubcomponent : AndroidInjector<MessagingService> {

    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<MessagingService>()
}
