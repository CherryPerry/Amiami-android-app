package ru.cherryperry.amiami.data.repository

import android.os.Build
import io.reactivex.schedulers.Schedulers
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import ru.cherryperry.amiami.domain.model.Filter

@Ignore
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class FilterRepositoryImplTest {

    private val filterRepositoryImpl = FilterRepositoryImpl(RuntimeEnvironment.application, Schedulers.trampoline())

    @Test
    fun testDefaultFilter() {
        filterRepositoryImpl
            .filter()
            .test()
            .awaitCount(1)
            .assertValue(Filter())
            .dispose()
    }

    @Test
    fun testSetMin() {
        val subscriber = filterRepositoryImpl
            .filter()
            .test()
            .awaitCount(1)
        filterRepositoryImpl
            .setMin(100)
            .test()
            .await()
            .assertComplete()
            .dispose()
        subscriber
            .awaitCount(2)
            .assertValues(
                Filter(),
                Filter(minPrice = 100))
            .dispose()
    }

    @Test
    fun testSetMax() {
        val subscriber = filterRepositoryImpl
            .filter()
            .test()
            .awaitCount(1)
        filterRepositoryImpl
            .setMax(100)
            .test()
            .await()
            .assertComplete()
            .dispose()
        subscriber
            .awaitCount(2)
            .assertValues(
                Filter(),
                Filter(maxPrice = 100))
            .dispose()
    }

    @Test
    fun testSetTerm() {
        val subscriber = filterRepositoryImpl
            .filter()
            .test()
            .awaitCount(1)
        filterRepositoryImpl
            .setTerm("test")
            .test()
            .await()
            .assertComplete()
            .dispose()
        subscriber
            .awaitCount(2)
            .assertValues(
                Filter(),
                Filter(textFilter = "test"))
            .dispose()
    }

    @Test
    fun testSetMinOverrideMax() {
        val subscriber = filterRepositoryImpl
            .filter()
            .test()
            .awaitCount(1)
        filterRepositoryImpl
            .setMax(10)
            .test()
            .await()
            .assertComplete()
            .dispose()
        filterRepositoryImpl
            .setMin(100)
            .test()
            .await()
            .assertComplete()
            .dispose()
        subscriber
            .awaitCount(3)
            .assertValueAt(subscriber.valueCount() - 1, Filter(100, 100, ""))
            .dispose()
    }

    @Test
    fun testSetMaxOverrideMin() {
        val subscriber = filterRepositoryImpl
            .filter()
            .test()
            .awaitCount(1)
        filterRepositoryImpl
            .setMin(100)
            .test()
            .await()
            .assertComplete()
            .dispose()
        filterRepositoryImpl
            .setMax(10)
            .test()
            .await()
            .assertComplete()
            .dispose()
        subscriber
            .awaitCount(3)
            .assertValueAt(subscriber.valueCount() - 1, Filter(10, 10, ""))
            .dispose()
    }

    @Test
    fun testReset() {
        filterRepositoryImpl
            .setMin(1)
            .test()
            .await()
            .assertComplete()
            .dispose()
        filterRepositoryImpl
            .setMax(2)
            .test()
            .await()
            .assertComplete()
            .dispose()
        filterRepositoryImpl
            .setTerm("test")
            .test()
            .await()
            .assertComplete()
            .dispose()
        filterRepositoryImpl
            .reset()
            .test()
            .await()
            .assertComplete()
            .dispose()
        filterRepositoryImpl
            .filter()
            .test()
            .awaitCount(1)
            .assertValue(Filter())
            .dispose()
    }
}
