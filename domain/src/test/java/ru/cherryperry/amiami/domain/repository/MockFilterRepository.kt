package ru.cherryperry.amiami.domain.repository

import ru.cherryperry.amiami.domain.model.Filter
import rx.Completable
import rx.Observable

open class MockFilterRepository : FilterRepository {

    override fun filter(): Observable<Filter> {
        throw NotImplementedError()
    }

    override fun setMin(value: Int): Completable {
        throw NotImplementedError()
    }

    override fun setMax(value: Int): Completable {
        throw NotImplementedError()
    }

    override fun setTerm(value: String): Completable {
        throw NotImplementedError()
    }
}
