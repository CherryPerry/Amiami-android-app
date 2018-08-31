package ru.cherryperry.amiami.domain.repository

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

interface PushNotificationService {

    /**
     * Notifications are enabled?
     */
    fun enabled(): Flowable<Boolean>

    /**
     * Enable notifications.
     */
    fun enable(): Completable

    /**
     * Disable notifications.
     */
    fun disable(): Completable

    /**
     * Current notification's counter value.
     * When application is in background, counter value is increased until app is opened.
     * It represents total number of updates, while app was in background.
     */
    fun counter(): Single<Int>

    /**
     * After showing notification counter should be increased.
     */
    fun setCounter(value: Int): Completable
}
