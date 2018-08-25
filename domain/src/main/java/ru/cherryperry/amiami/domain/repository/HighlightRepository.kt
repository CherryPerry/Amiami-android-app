package ru.cherryperry.amiami.domain.repository

import io.reactivex.Completable
import io.reactivex.Flowable
import ru.cherryperry.amiami.domain.model.HighlightConfiguration
import ru.cherryperry.amiami.domain.model.HighlightRule

interface HighlightRepository {

    fun configuration(): Flowable<HighlightConfiguration>

    fun add(value: HighlightRule): Completable

    fun remove(id: Long): Completable

    fun replace(list: List<HighlightRule>): Completable
}
