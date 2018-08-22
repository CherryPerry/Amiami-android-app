package ru.cherryperry.amiami.domain.notifications

import ru.cherryperry.amiami.domain.FlowableUseCase
import ru.cherryperry.amiami.domain.repository.PushNotificationService
import javax.inject.Inject

class ObserveNotificationsSettingUseCase @Inject constructor(
    private val pushNotificationService: PushNotificationService
) : FlowableUseCase<Unit, Boolean>() {

    override fun run(param: Unit) = pushNotificationService.enabled()
}
