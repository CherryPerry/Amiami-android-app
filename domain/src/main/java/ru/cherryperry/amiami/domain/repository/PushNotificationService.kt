package ru.cherryperry.amiami.domain.repository

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single

interface PushNotificationService {

    /**
     * Notifications are enabled?
     */
    fun enabled(): Flowable<Boolean>

    /**
     * Enable notifications.
     * Changing this setting must call [resetCounter].
     */
    fun enable(): Completable

    /**
     * Disable notifications.
     * Changing this setting must call [resetCounter].
     */
    fun disable(): Completable

    /**
     * Current notification's counter value.
     * When application is in background, counter value is increased until app is opened.
     * It represents total number of updates, while app was in background.
     */
    fun counter(): Single<Int>

    /**
     * Increase counter by value amd return new value.
     * Value can be filtered by minimum notification count setting.
     */
    fun increaseCounterAndReturnIfValid(value: Int): Maybe<Int>

    /**
     * Set counter to ```0```.
     */
    fun resetCounter(): Completable

    /**
     * Set minimum notification count setting.
     */
    fun setPushCounterFilter(value: Int): Completable
}
