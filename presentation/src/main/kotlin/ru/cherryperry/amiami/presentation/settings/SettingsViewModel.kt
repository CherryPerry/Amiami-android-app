package ru.cherryperry.amiami.presentation.settings

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.OnLifecycleEvent
import ru.cherryperry.amiami.domain.notifications.ObserveNotificationsSettingUseCase
import ru.cherryperry.amiami.domain.notifications.SubscribeToNotificationsUseCase
import ru.cherryperry.amiami.presentation.base.BaseViewModel
import rx.android.schedulers.AndroidSchedulers
import rx.subscriptions.Subscriptions
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    private val getCurrentRatesUseCase: ru.cherryperry.amiami.domain.currency.GetCurrentRatesUseCase,
    private val observeNotificationsSettingUseCase: ObserveNotificationsSettingUseCase,
    private val subscribeToNotificationsUseCase: SubscribeToNotificationsUseCase
) : BaseViewModel(), LifecycleObserver {

    private var notificationSubscription = Subscriptions.unsubscribed()

    val currencySetting: LiveData<CurrencySetting> by lazy {
        val liveData = MutableLiveData<CurrencySetting>()
        liveData.value = CurrencySetting(false, emptyArray(), emptyArray())
        this += getCurrentRatesUseCase.run(Any())
            .map { it.currencies.toTypedArray<CharSequence>() }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ liveData.value = CurrencySetting(true, it, it) }, { it.printStackTrace() })
        liveData
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
}
