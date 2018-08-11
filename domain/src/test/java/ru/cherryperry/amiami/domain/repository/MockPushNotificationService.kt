package ru.cherryperry.amiami.domain.repository

import rx.Completable
import rx.Observable

open class MockPushNotificationService : PushNotificationService {

    override fun enabled(): Observable<Boolean> {
        throw NotImplementedError()
    }

    override fun enable(): Completable {
        throw NotImplementedError()
    }

    override fun disable(): Completable {
        throw NotImplementedError()
    }
}
