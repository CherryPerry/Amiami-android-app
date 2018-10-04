package ru.cherryperry.amiami.domain.repository

import io.reactivex.Completable
import io.reactivex.Flowable
import ru.cherryperry.amiami.domain.model.Filter

interface FilterRepository {

    fun filter(): Flowable<Filter>

    fun setMin(value: Int): Completable

    fun setMax(value: Int): Completable

    fun setTerm(value: String): Completable

    fun reset(): Completable
}
