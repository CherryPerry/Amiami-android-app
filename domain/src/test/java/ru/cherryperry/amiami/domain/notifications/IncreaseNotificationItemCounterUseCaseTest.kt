package ru.cherryperry.amiami.domain.notifications

import io.reactivex.Flowable
import io.reactivex.Maybe
import org.junit.Test
import org.mockito.Mockito
import ru.cherryperry.amiami.domain.repository.PushNotificationService

class IncreaseNotificationItemCounterUseCaseTest {

    @Test
    fun testNotificationsEnabled() {
        val service = Mockito.mock(PushNotificationService::class.java)
        Mockito.`when`(service.enabled()).thenReturn(Flowable.just(true))
        Mockito.`when`(service.increaseCounterAndReturnIfValid(1)).thenReturn(Maybe.just(2))
        IncreaseNotificationItemCounterUseCase(service).run(1)
            .test()
            .await()
            .assertComplete()
            .assertValue(2)
            .dispose()
        Mockito.verify(service).enabled()
        Mockito.verify(service).increaseCounterAndReturnIfValid(1)
    }

    @Test
    fun testNotificationsDisabled() {
        val service = Mockito.mock(PushNotificationService::class.java)
        Mockito.`when`(service.enabled()).thenReturn(Flowable.just(false))
        IncreaseNotificationItemCounterUseCase(service).run(1)
            .test()
            .await()
            .assertComplete()
            .assertNoValues()
            .dispose()
        Mockito.verify(service).enabled()
        Mockito.verify(service, Mockito.never()).increaseCounterAndReturnIfValid(Mockito.anyInt())
    }

    @Test
    fun testUseOnlyFirstResult() {
        val service = Mockito.mock(PushNotificationService::class.java)
        Mockito.`when`(service.enabled()).thenReturn(Flowable.just(false, true))
        IncreaseNotificationItemCounterUseCase(service).run(1)
            .test()
            .await()
            .assertComplete()
            .assertNoValues()
            .dispose()
        Mockito.verify(service).enabled()
        Mockito.verify(service, Mockito.never()).increaseCounterAndReturnIfValid(Mockito.anyInt())
    }
}
