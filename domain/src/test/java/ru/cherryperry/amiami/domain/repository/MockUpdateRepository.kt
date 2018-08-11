package ru.cherryperry.amiami.domain.repository

import ru.cherryperry.amiami.domain.model.UpdateInfo
import rx.Observable

open class MockUpdateRepository : UpdateRepository {

    override fun latestRelease(): Observable<UpdateInfo> {
        throw NotImplementedError()
    }
}
