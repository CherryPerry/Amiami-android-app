package ru.cherryperry.amiami.data.prefs

import android.content.SharedPreferences
import androidx.core.content.edit
import rx.Emitter
import rx.Observable
import rx.schedulers.Schedulers
import java.util.concurrent.Executors

private val scheduler = Schedulers.from(Executors.newSingleThreadExecutor())

abstract class BasePreference<Type>(
    protected val key: String,
    protected val defaultValue: Type,
    protected val sharedPreferences: SharedPreferences
) {

    abstract var value: Type

    val observer: Observable<Type> = Observable.create<Type>(
        { emitter ->
            val callback = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
                if (key == this.key) {
                    emitter.onNext(value)
                }
            }
            sharedPreferences.registerOnSharedPreferenceChangeListener(callback)
            emitter.setCancellation { sharedPreferences.unregisterOnSharedPreferenceChangeListener(callback) }
            emitter.onNext(value)
        }, Emitter.BackpressureMode.LATEST)
        .observeOn(scheduler)
        .replay(1)
        .refCount()
}

class IntPreference(
    key: String,
    defaultValue: Int,
    sharedPreferences: SharedPreferences
) : BasePreference<Int>(key, defaultValue, sharedPreferences) {

    override var value: Int
        get() = sharedPreferences.getInt(key, defaultValue)
        @SuppressWarnings("CommitPrefEdits")
        // it is false positive
        set(value) {
            sharedPreferences.edit { putInt(key, value) }
        }
}

class StringPreference(
    key: String,
    defaultValue: String,
    sharedPreferences: SharedPreferences
) : BasePreference<String>(key, defaultValue, sharedPreferences) {

    override var value: String
        get() = sharedPreferences.getString(key, defaultValue)
        @SuppressWarnings("CommitPrefEdits")
        // it is false positive
        set(value) {
            sharedPreferences.edit { putString(key, value) }
        }
}

class BooleanPreference(
    key: String,
    defaultValue: Boolean,
    sharedPreferences: SharedPreferences
) : BasePreference<Boolean>(key, defaultValue, sharedPreferences) {

    override var value: Boolean
        get() = sharedPreferences.getBoolean(key, defaultValue)
        @SuppressWarnings("CommitPrefEdits")
        // it is false positive
        set(value) {
            sharedPreferences.edit { putBoolean(key, value) }
        }
}

class StringSetPreference(
    key: String,
    defaultValue: Set<String>,
    sharedPreferences: SharedPreferences
) : BasePreference<Set<String>>(key, defaultValue, sharedPreferences) {

    override var value: Set<String>
        get() = sharedPreferences.getStringSet(key, defaultValue)
        @SuppressWarnings("CommitPrefEdits")
        // it is false positive
        set(value) {
            sharedPreferences.edit { putStringSet(key, value) }
        }
}
