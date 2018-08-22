package ru.cherryperry.amiami.domain.repository

import io.reactivex.Completable
import io.reactivex.Flowable

interface PushNotificationService {

    fun enabled(): Flowable<Boolean>

    fun enable(): Completable

    fun disable(): Completable
}
