package ru.cherryperry.amiami.presentation.base

import android.arch.lifecycle.ViewModel
import android.support.annotation.CallSuper
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseViewModel : ViewModel() {

    private val subscriptions = CompositeDisposable()

    operator fun plusAssign(subscription: Disposable) {
        subscriptions.add(subscription)
    }

    @CallSuper
    override fun onCleared() {
        super.onCleared()
        subscriptions.clear()
    }
}
