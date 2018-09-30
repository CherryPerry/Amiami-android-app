package ru.cherryperry.amiami.data.prefs

import android.os.Build
import android.preference.PreferenceManager
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class IntPreferenceTest {

    companion object {
        private const val KEY = "key"
    }

    private val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(RuntimeEnvironment.application)
    private val preference = IntPreference(KEY, 1, sharedPreferences)

    @Test
    fun testGetDefaultValue() {
        Assert.assertEquals(1, preference.value)
        // value is really unset
        Assert.assertEquals(0, sharedPreferences.getInt(KEY, 0))
    }

    @Test
    fun testSetAndGet() {
        preference.value = 0
        Assert.assertEquals(0, preference.value)
        Assert.assertEquals(0, sharedPreferences.getInt(KEY, 1))
    }

    @Test
    fun testSubscription() {
        val subscriber = preference
            .observer
            .test()
            .awaitCount(1)
        preference.value = 2
        subscriber
            .awaitCount(2)
            .assertValues(1, 2)
            .dispose()
    }
}
