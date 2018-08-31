package ru.cherryperry.amiami.domain.notifications

import io.reactivex.Completable
import ru.cherryperry.amiami.domain.CompletableUseCase
import ru.cherryperry.amiami.domain.repository.PushNotificationService
import javax.inject.Inject

class ResetNotificationItemCounterUseCase @Inject constructor(
    private val pushNotificationService: PushNotificationService
) : CompletableUseCase<Unit>() {

    override fun run(param: Unit): Completable = pushNotificationService.setCounter(0)
}
