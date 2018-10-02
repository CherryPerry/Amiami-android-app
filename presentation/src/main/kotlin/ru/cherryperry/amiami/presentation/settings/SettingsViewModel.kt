package ru.cherryperry.amiami.presentation.settings

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import ru.cherryperry.amiami.domain.currency.GetCurrentRatesUseCase
import ru.cherryperry.amiami.domain.notifications.ObserveNotificationsSettingUseCase
import ru.cherryperry.amiami.domain.notifications.SetNotificationFilterValueUseCase
import ru.cherryperry.amiami.domain.notifications.SubscribeToNotificationsUseCase
import ru.cherryperry.amiami.presentation.base.BaseViewModel
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    private val getCurrentRatesUseCase: GetCurrentRatesUseCase,
    private val observeNotificationsSettingUseCase: ObserveNotificationsSettingUseCase,
    private val subscribeToNotificationsUseCase: SubscribeToNotificationsUseCase,
    pushCounterFilterStringObserver: PushCounterFilterStringObserver,
    setNotificationFilterValueUseCase: SetNotificationFilterValueUseCase
) : BaseViewModel() {

    val currencySetting: LiveData<CurrencySetting> by lazy {
        val liveData = MutableLiveData<CurrencySetting>()
        liveData.value = CurrencySetting(false, emptyArray(), emptyArray())
        this += getCurrentRatesUseCase.run(Unit)
            .map { it.currencies.toTypedArray<CharSequence>() }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ liveData.value = CurrencySetting(true, it, it) }, { it.printStackTrace() })
        liveData
    }
    val notificationsEnabled: LiveData<Boolean> by lazy {
        val data = MutableLiveData<Boolean>()
        this += observeNotificationsSettingUseCase.run(Unit)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { data.value = it }
        data
    }

    init {
        // update real value after changing fake one
        // this screen is only place, where it can be changed
        this += pushCounterFilterStringObserver.observeStringPreference()
            .switchMapCompletable { string ->
                string.toIntOrNull()?.let { value ->
                    setNotificationFilterValueUseCase.run(value)
                } ?: Completable.complete()
            }
            .subscribe()
        // after setting change we must subscribe/unsubscribe from notifications
        // TODO Move this logic to PushNotificationService
        this += observeNotificationsSettingUseCase.run(Unit)
            .skip(1)
            .switchMapCompletable { subscribeToNotificationsUseCase.run(it) }
            .subscribe()
    }
}
