package ru.cherryperry.amiami.data.repository

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.google.firebase.messaging.FirebaseMessaging
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Scheduler
import io.reactivex.Single
import ru.cherryperry.amiami.core.createBackgroundThreadScheduler
import ru.cherryperry.amiami.data.R
import ru.cherryperry.amiami.data.prefs.BooleanPreference
import ru.cherryperry.amiami.data.prefs.IntPreference
import ru.cherryperry.amiami.domain.repository.PushNotificationService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PushNotificationServiceImpl constructor(
    context: Context,
    private val firebaseMessaging: FirebaseMessaging,
    private val scheduler: Scheduler
) : PushNotificationService {

    companion object {
        private const val TOPIC_UPDATES = "updates2"
    }

    @Inject
    constructor(context: Context, firebaseMessaging: FirebaseMessaging) : this(
        context, firebaseMessaging, createBackgroundThreadScheduler("PushNotificationServiceThread"))

    private val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    private val enabledPreference = BooleanPreference(context.getString(R.string.key_push), false,
        preferences, scheduler)
    private val itemCounterPreference = IntPreference(context.getString(R.string.key_push_counter), 0,
        preferences, scheduler)
    private val counterFilterPreference = IntPreference(context.getString(R.string.key_push_counter_filter), 0,
        preferences, scheduler)

    override fun enabled() = enabledPreference.observer

    override fun enable() = call(true)

    override fun disable() = call(false)

    private fun call(param: Boolean): Completable = Completable
        .create { emitter ->
            val task = firebaseMessaging.run {
                if (param) {
                    subscribeToTopic(TOPIC_UPDATES)
                } else {
                    unsubscribeFromTopic(TOPIC_UPDATES)
                }
            }
            task.addOnSuccessListener { emitter.onComplete() }
            task.addOnFailureListener { emitter.onError(it) }
        }
        .andThen(resetCounter())
        .andThen(Completable.fromAction { enabledPreference.value = param })
        .onErrorResumeNext { Completable.fromAction { enabledPreference.value = !param } }
        .subscribeOn(scheduler)

    override fun counter(): Single<Int> = Single
        .just(itemCounterPreference.value)
        .subscribeOn(scheduler)

    override fun increaseCounterAndReturnIfValid(value: Int): Maybe<Int> = Single
        .fromCallable {
            val newValue = itemCounterPreference.value + value
            itemCounterPreference.value = newValue
            newValue
        }
        .filter { it >= counterFilterPreference.value }
        .subscribeOn(scheduler)

    override fun resetCounter(): Completable = Completable
        .fromAction { itemCounterPreference.value = 0 }
        .subscribeOn(scheduler)

    override fun setPushCounterFilter(value: Int): Completable = Completable
        .fromAction { counterFilterPreference.value = value }
        .subscribeOn(scheduler)
}
