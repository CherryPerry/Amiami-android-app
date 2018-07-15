package ru.cherryperry.amiami.screen.settings

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.OnLifecycleEvent
import ru.cherryperry.amiami.domain.currency.GetCurrentRatesUseCase
import ru.cherryperry.amiami.domain.notifications.ObserveNotificationsSettingUseCase
import ru.cherryperry.amiami.domain.notifications.SubscribeToNotificationsUseCase
import ru.cherryperry.amiami.screen.base.BaseViewModel
import rx.subscriptions.Subscriptions
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    private val getCurrentRatesUseCase: GetCurrentRatesUseCase,
    private val observeNotificationsSettingUseCase: ObserveNotificationsSettingUseCase,
    private val subscribeToNotificationsUseCase: SubscribeToNotificationsUseCase
) : BaseViewModel(), LifecycleObserver {

    private val _currencySetting = MutableLiveData<CurrencySetting>()
    private var notificationSubscription = Subscriptions.unsubscribed()

    val currencySetting: LiveData<CurrencySetting>
        get() {
            if (_currencySetting.value == null) {
                _currencySetting.value = CurrencySetting(false, emptyArray(), emptyArray())
                loadCurrencySettings()
            }
            return _currencySetting
        }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun startObservePreferenceChanges() {
        notificationSubscription = observeNotificationsSettingUseCase.run(Any())
            .flatMapCompletable { subscribeToNotificationsUseCase.run(it) }
            .subscribe()
        this += notificationSubscription
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun stopObservePreferenceChanges() {
        notificationSubscription.unsubscribe()
    }

    private fun loadCurrencySettings() {
        this += getCurrentRatesUseCase.run(Any())
            .subscribe({
                val array: Array<CharSequence> = it.rates.keys.toTypedArray()
                _currencySetting.postValue(CurrencySetting(true, array, array))
            }, {})
    }
}