package ru.cherryperry.amiami.domain.notifications

import io.reactivex.Completable
import ru.cherryperry.amiami.domain.CompletableUseCase
import ru.cherryperry.amiami.domain.repository.PushNotificationService
import javax.inject.Inject

class SetNotificationFilterValueUseCase @Inject constructor(
    private val pushNotificationService: PushNotificationService
) : CompletableUseCase<Int>() {

    override fun run(param: Int): Completable = pushNotificationService.setPushCounterFilter(param)
}
