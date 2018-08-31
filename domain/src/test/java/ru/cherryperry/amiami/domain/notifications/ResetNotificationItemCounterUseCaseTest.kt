package ru.cherryperry.amiami.domain.notifications

import io.reactivex.Completable
import org.junit.Test
import org.mockito.Mockito
import ru.cherryperry.amiami.domain.repository.PushNotificationService

class ResetNotificationItemCounterUseCaseTest {

    @Test
    fun testSetCounterIsZero() {
        val service = Mockito.mock(PushNotificationService::class.java)
        Mockito.`when`(service.setCounter(0)).thenReturn(Completable.complete())
        val useCase = ResetNotificationItemCounterUseCase(service)
        useCase.run(Unit)
            .test()
            .await()
            .assertComplete()
        Mockito.verify(service).setCounter(0)
    }
}
