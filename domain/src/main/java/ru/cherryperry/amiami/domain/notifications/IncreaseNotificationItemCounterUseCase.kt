package ru.cherryperry.amiami.domain.notifications

import io.reactivex.Maybe
import ru.cherryperry.amiami.domain.MaybeUseCase
import ru.cherryperry.amiami.domain.repository.PushNotificationService
import javax.inject.Inject

/**
 * Increase counter and return it.
 * If notifications are disabled, nothing is increased and no value is returned.
 */
class IncreaseNotificationItemCounterUseCase @Inject constructor(
    private val pushNotificationService: PushNotificationService
) : MaybeUseCase<Int, Int>() {

    override fun run(param: Int): Maybe<Int> =
        pushNotificationService.enabled()
            .firstElement()
            .filter { it }
            .flatMap { pushNotificationService.increaseCounterAndReturnIfValid(param) }
}
