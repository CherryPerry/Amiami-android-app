package ru.cherryperry.amiami.data.prefs

import android.content.SharedPreferences
import androidx.core.content.edit
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Scheduler
import ru.cherryperry.amiami.core.createBackgroundThreadScheduler

private val defaultScheduler = createBackgroundThreadScheduler("SharedPreferenceThread")

abstract class BasePreference<Type>(
    protected val key: String,
    protected val defaultValue: Type,
    protected val sharedPreferences: SharedPreferences,
    protected val scheduler: Scheduler = defaultScheduler
) {

    abstract var value: Type

    abstract val nullableValue: Type?

    private val internalObserver: Flowable<Type> by lazy {
        Flowable.create<Type>(
            { emitter ->
                val callback = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
                    if (key == this.key) {
                        emitter.onNext(value)
                    }
                }
                sharedPreferences.registerOnSharedPreferenceChangeListener(callback)
                emitter.setCancellable { sharedPreferences.unregisterOnSharedPreferenceChangeListener(callback) }
                emitter.onNext(value)
            }, BackpressureStrategy.LATEST)
            .subscribeOn(scheduler)
            .unsubscribeOn(scheduler)
            .observeOn(scheduler)
            .replay(1)
            .refCount()
    }

    val observer: Flowable<Type>
        get() = internalObserver
}

class IntPreference(
    key: String,
    defaultValue: Int,
    sharedPreferences: SharedPreferences,
    scheduler: Scheduler = defaultScheduler
) : BasePreference<Int>(key, defaultValue, sharedPreferences, scheduler) {

    override var value: Int
        get() = sharedPreferences.getInt(key, defaultValue)
        set(value) {
            sharedPreferences.edit { putInt(key, value) }
        }

    override val nullableValue: Int?
        get() = sharedPreferences.run {
            if (contains(key)) {
                getInt(key, defaultValue)
            } else {
                null
            }
        }
}

class StringPreference(
    key: String,
    defaultValue: String,
    sharedPreferences: SharedPreferences,
    scheduler: Scheduler = defaultScheduler
) : BasePreference<String>(key, defaultValue, sharedPreferences, scheduler) {

    override var value: String
        get() = sharedPreferences.getString(key, defaultValue)!!
        set(value) {
            sharedPreferences.edit { putString(key, value) }
        }

    override val nullableValue: String?
        get() = sharedPreferences.getString(key, null)
}

class BooleanPreference(
    key: String,
    defaultValue: Boolean,
    sharedPreferences: SharedPreferences,
    scheduler: Scheduler = defaultScheduler
) : BasePreference<Boolean>(key, defaultValue, sharedPreferences, scheduler) {

    override var value: Boolean
        get() = sharedPreferences.getBoolean(key, defaultValue)
        set(value) {
            sharedPreferences.edit { putBoolean(key, value) }
        }

    override val nullableValue: Boolean?
        get() = sharedPreferences.run {
            if (contains(key)) {
                getBoolean(key, defaultValue)
            } else {
                null
            }
        }
}
