package ru.cherryperry.amiami.domain.notifications

import com.google.firebase.messaging.FirebaseMessaging
import ru.cherryperry.amiami.AppPrefs
import ru.cherryperry.amiami.domain.CompletableUseCase
import ru.cherryperry.amiami.push.MessagingService
import rx.Completable
import javax.inject.Inject

class SubscribeToNotificationsUseCase @Inject constructor(
        private val appPrefs: AppPrefs
) : CompletableUseCase<Boolean>() {

    override fun run(param: Boolean): Completable {
        return Completable
                .fromEmitter { emitter ->
                    val task = FirebaseMessaging.getInstance().run {
                        if (param) {
                            subscribeToTopic(MessagingService.updateTopic)
                        } else {
                            unsubscribeFromTopic(MessagingService.updateTopic)
                        }
                    }
                    task.addOnSuccessListener { emitter.onCompleted() }
                    task.addOnFailureListener { emitter.onError(it) }
                }
                .onErrorResumeNext { Completable.fromAction { appPrefs.push = !param } }
    }
}