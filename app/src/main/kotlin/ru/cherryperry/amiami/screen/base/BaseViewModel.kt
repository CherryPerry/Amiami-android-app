package ru.cherryperry.amiami.screen.base

import android.arch.lifecycle.ViewModel
import android.support.annotation.CallSuper
import rx.Subscription
import rx.subscriptions.CompositeSubscription

abstract class BaseViewModel : ViewModel() {

    private val subscriptions = CompositeSubscription()

    operator fun plusAssign(subscription: Subscription) {
        subscriptions.add(subscription)
    }

    @CallSuper
    override fun onCleared() {
        super.onCleared()
        subscriptions.clear()
    }
}