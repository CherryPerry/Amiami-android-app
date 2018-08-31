package ru.cherryperry.amiami.domain.notifications

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import org.junit.Test
import org.mockito.Mockito
import ru.cherryperry.amiami.domain.repository.PushNotificationService

class IncreaseNotificationItemCounterUseCaseTest {

    @Test
    fun testNotificationsEnabled() {
        val service = Mockito.mock(PushNotificationService::class.java)
        Mockito.`when`(service.enabled()).thenReturn(Flowable.just(true))
        Mockito.`when`(service.counter()).thenReturn(Single.just(1))
        Mockito.`when`(service.setCounter(2)).thenReturn(Completable.complete())
        val useCase = IncreaseNotificationItemCounterUseCase(service)
        useCase.run(1)
            .test()
            .await()
            .assertComplete()
            .assertValue(2)
    }

    @Test
    fun testNotificationsDisabled() {
        val service = Mockito.mock(PushNotificationService::class.java)
        Mockito.`when`(service.enabled()).thenReturn(Flowable.just(false))
        val useCase = IncreaseNotificationItemCounterUseCase(service)
        useCase.run(1)
            .test()
            .await()
            .assertComplete()
            .assertNoValues()
    }

    @Test
    fun testUseOnlyFirstResult() {
        val service = Mockito.mock(PushNotificationService::class.java)
        Mockito.`when`(service.enabled()).thenReturn(Flowable.just(false, true))
        val useCase = IncreaseNotificationItemCounterUseCase(service)
        useCase.run(1)
            .test()
            .await()
            .assertComplete()
            .assertNoValues()
    }
}
