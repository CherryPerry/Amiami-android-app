package ru.cherryperry.amiami.domain.repository

import rx.Completable
import rx.Observable

interface PushNotificationService {

    fun enabled(): Observable<Boolean>

    fun enable(): Completable

    fun disable(): Completable
}
