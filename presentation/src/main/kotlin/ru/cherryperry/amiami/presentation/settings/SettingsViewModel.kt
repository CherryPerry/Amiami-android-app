package ru.cherryperry.amiami.presentation.settings

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.OnLifecycleEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import ru.cherryperry.amiami.domain.currency.GetCurrentRatesUseCase
import ru.cherryperry.amiami.domain.notifications.ObserveNotificationsSettingUseCase
import ru.cherryperry.amiami.domain.notifications.SubscribeToNotificationsUseCase
import ru.cherryperry.amiami.presentation.base.BaseViewModel
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    private val getCurrentRatesUseCase: GetCurrentRatesUseCase,
    private val observeNotificationsSettingUseCase: ObserveNotificationsSettingUseCase,
    private val subscribeToNotificationsUseCase: SubscribeToNotificationsUseCase
) : BaseViewModel(), LifecycleObserver {

    private var notificationSubscription = Disposables.disposed()

    val currencySetting: LiveData<CurrencySetting> by lazy {
        val liveData = MutableLiveData<CurrencySetting>()
        liveData.value = CurrencySetting(false, emptyArray(), emptyArray())
        this += getCurrentRatesUseCase.run(Unit)
            .map { it.currencies.toTypedArray<CharSequence>() }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ liveData.value = CurrencySetting(true, it, it) }, { it.printStackTrace() })
        liveData
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun startObservePreferenceChanges() {
        notificationSubscription = observeNotificationsSettingUseCase.run(Unit)
            .flatMapCompletable { subscribeToNotificationsUseCase.run(it) }
            .subscribe()
        this += notificationSubscription
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun stopObservePreferenceChanges() {
        notificationSubscription.dispose()
    }
}
