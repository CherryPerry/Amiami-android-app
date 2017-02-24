package ru.cherryperry.amiami.push

import dagger.Component
import ru.cherryperry.amiami.ApplicationModule
import javax.inject.Singleton

@Component(modules = arrayOf(
        ApplicationModule::class
))
@Singleton
interface MessagingServiceComponent {
    fun inject(messagingService: MessagingService)
}