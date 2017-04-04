package ru.cherryperry.amiami.screen.base

import com.arellomobile.mvp.MvpPresenter
import com.arellomobile.mvp.MvpView
import rx.Subscription
import rx.subscriptions.CompositeSubscription

/**
 * Base presenter for presenters
 * Contains CompositeSubscription to unsubscribe on destroy
 */
open class BasePresenter<View : MvpView> : MvpPresenter<View>() {

    private val compositeSubscription = CompositeSubscription()

    fun addSubscription(subscription: Subscription) {
        compositeSubscription.add(subscription)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeSubscription.clear()
    }
}