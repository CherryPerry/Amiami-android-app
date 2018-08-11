package ru.cherryperry.amiami.domain.notifications

import ru.cherryperry.amiami.domain.ObservableUseCase
import ru.cherryperry.amiami.domain.repository.PushNotificationService
import javax.inject.Inject

class ObserveNotificationsSettingUseCase @Inject constructor(
    private val pushNotificationService: PushNotificationService
) : ObservableUseCase<Any, Boolean>() {

    override fun run(param: Any) = pushNotificationService.enabled()
}
