package ru.cherryperry.amiami.data.repository

import android.os.Build
import com.google.android.gms.tasks.Tasks
import com.google.firebase.messaging.FirebaseMessaging
import io.reactivex.schedulers.Schedulers
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class PushNotificationServiceImplTest {

    @Test
    fun testEnabled() {
        val filterRepositoryImpl = PushNotificationServiceImpl(RuntimeEnvironment.application,
            Mockito.mock(FirebaseMessaging::class.java), Schedulers.trampoline())
        filterRepositoryImpl.enabled()
            .test()
            .awaitCount(1)
            .assertValue(false)
            .dispose()
    }

    @Test
    fun testEnableAndDisable() {
        val messaging = Mockito.mock(FirebaseMessaging::class.java)
        Mockito.`when`(messaging.subscribeToTopic(Mockito.anyString())).thenReturn(Tasks.forResult(null))
        Mockito.`when`(messaging.unsubscribeFromTopic(Mockito.anyString())).thenReturn(Tasks.forResult(null))
        val filterRepositoryImpl = PushNotificationServiceImpl(RuntimeEnvironment.application,
            messaging, Schedulers.trampoline())
        val test = filterRepositoryImpl.enabled()
            .test()
            .awaitCount(1)
        filterRepositoryImpl.enable()
            .test()
            .await()
            .assertComplete()
            .dispose()
        Mockito.verify(messaging).subscribeToTopic(Mockito.anyString())
        test.awaitCount(2)
        filterRepositoryImpl.disable()
            .test()
            .await()
            .assertComplete()
            .dispose()
        Mockito.verify(messaging).unsubscribeFromTopic(Mockito.anyString())
        test.awaitCount(3)
            .assertValues(false, true, false)
            .dispose()
    }

    @Test
    fun testCounter() {
        val filterRepositoryImpl = PushNotificationServiceImpl(RuntimeEnvironment.application,
            Mockito.mock(FirebaseMessaging::class.java), Schedulers.trampoline())
        filterRepositoryImpl.counter()
            .test()
            .await()
            .assertValue(0)
            .dispose()
    }

    @Test
    fun testIncreaseCounterAndReturnIfValid() {
        val filterRepositoryImpl = PushNotificationServiceImpl(RuntimeEnvironment.application,
            Mockito.mock(FirebaseMessaging::class.java), Schedulers.trampoline())
        filterRepositoryImpl.increaseCounterAndReturnIfValid(1)
            .test()
            .await()
            .assertComplete()
            .dispose()
        filterRepositoryImpl.counter()
            .test()
            .await()
            .assertValue(1)
            .dispose()
    }

    @Test
    fun testResetCounter() {
        val filterRepositoryImpl = PushNotificationServiceImpl(RuntimeEnvironment.application,
            Mockito.mock(FirebaseMessaging::class.java), Schedulers.trampoline())
        filterRepositoryImpl.increaseCounterAndReturnIfValid(1)
            .test()
            .await()
            .assertComplete()
            .dispose()
        filterRepositoryImpl.resetCounter()
            .test()
            .await()
            .assertComplete()
            .dispose()
        filterRepositoryImpl.counter()
            .test()
            .await()
            .assertValue(0)
            .dispose()
    }

    @Test
    fun testIncreaseCounterAndReturnIfValidFilter() {
        val filterRepositoryImpl = PushNotificationServiceImpl(RuntimeEnvironment.application,
            Mockito.mock(FirebaseMessaging::class.java), Schedulers.trampoline())
        filterRepositoryImpl.setPushCounterFilter(10)
            .test()
            .await()
            .assertComplete()
            .dispose()
        filterRepositoryImpl.increaseCounterAndReturnIfValid(1)
            .test()
            .await()
            .assertNoValues()
            .dispose()
        filterRepositoryImpl.increaseCounterAndReturnIfValid(10)
            .test()
            .await()
            .assertValue(11)
            .dispose()
    }
}
