package ru.cherryperry.amiami.domain.notifications

import io.reactivex.Completable
import org.junit.Test
import org.mockito.Mockito
import ru.cherryperry.amiami.domain.repository.PushNotificationService

class ResetNotificationItemCounterUseCaseTest {

    @Test
    fun testSetCounterIsZero() {
        val service = Mockito.mock(PushNotificationService::class.java)
        Mockito.`when`(service.resetCounter()).thenReturn(Completable.complete())
        ResetNotificationItemCounterUseCase(service).run(Unit)
            .test()
            .await()
            .assertComplete()
            .dispose()
        Mockito.verify(service).resetCounter()
    }
}
