package ru.cherryperry.amiami.domain.notifications

import io.reactivex.Completable
import org.junit.Test
import org.mockito.Mockito
import ru.cherryperry.amiami.domain.repository.PushNotificationService

class SubscribeToNotificationsUseCaseTest {

    @Test
    fun testEnable() {
        val service = Mockito.mock(PushNotificationService::class.java)
        Mockito.`when`(service.enable()).thenReturn(Completable.complete())
        SubscribeToNotificationsUseCase(service)
            .run(true)
            .test()
            .await()
            .assertComplete()
            .dispose()
        Mockito.verify(service).enable()
    }

    @Test
    fun testDisable() {
        val service = Mockito.mock(PushNotificationService::class.java)
        Mockito.`when`(service.disable()).thenReturn(Completable.complete())
        SubscribeToNotificationsUseCase(service)
            .run(false)
            .test()
            .await()
            .assertComplete()
            .dispose()
        Mockito.verify(service).disable()
    }
}
