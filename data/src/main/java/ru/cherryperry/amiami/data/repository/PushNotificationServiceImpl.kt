package ru.cherryperry.amiami.data.repository

import com.google.firebase.messaging.FirebaseMessaging
import io.reactivex.Completable
import ru.cherryperry.amiami.data.prefs.AppPrefs
import ru.cherryperry.amiami.domain.repository.PushNotificationService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PushNotificationServiceImpl @Inject constructor(
    private val appPrefs: AppPrefs
) : PushNotificationService {

    companion object {
        private const val TOPIC_UPDATES = "updates2"
    }

    override fun enabled() = appPrefs.push.observer

    override fun enable() = call(true)

    override fun disable() = call(false)

    private fun call(param: Boolean): Completable = Completable
        .create { emitter ->
            val task = FirebaseMessaging.getInstance().run {
                if (param) {
                    subscribeToTopic(TOPIC_UPDATES)
                } else {
                    unsubscribeFromTopic(TOPIC_UPDATES)
                }
            }
            task.addOnSuccessListener { emitter.onComplete() }
            task.addOnFailureListener { emitter.onError(it) }
        }
        .onErrorResumeNext { Completable.fromAction { appPrefs.push.value = !param } }
}
