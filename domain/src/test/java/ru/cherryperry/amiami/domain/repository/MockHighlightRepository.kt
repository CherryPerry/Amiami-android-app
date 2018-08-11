package ru.cherryperry.amiami.domain.repository

import ru.cherryperry.amiami.domain.model.HighlightConfiguration
import ru.cherryperry.amiami.domain.model.HighlightRule
import rx.Completable
import rx.Observable

open class MockHighlightRepository : HighlightRepository {

    override fun list(): Observable<List<String>> {
        throw NotImplementedError()
    }

    override fun configuration(): Observable<HighlightConfiguration> {
        throw NotImplementedError()
    }

    override fun add(value: String): Completable {
        throw NotImplementedError()
    }

    override fun remove(value: String): Completable {
        throw NotImplementedError()
    }

    override fun replace(list: List<HighlightRule>): Completable {
        throw NotImplementedError()
    }
}
