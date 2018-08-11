package ru.cherryperry.amiami.domain.repository

import ru.cherryperry.amiami.domain.model.Filter
import rx.Completable
import rx.Observable

interface FilterRepository {

    fun filter(): Observable<Filter>

    fun setMin(value: Int): Completable

    fun setMax(value: Int): Completable

    fun setTerm(value: String): Completable
}
