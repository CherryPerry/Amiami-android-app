package ru.cherryperry.amiami.presentation.base

import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModel
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
