package ru.cherryperry.amiami.domain.repository

import ru.cherryperry.amiami.domain.model.HighlightConfiguration
import ru.cherryperry.amiami.domain.model.HighlightRule
import rx.Completable
import rx.Observable

interface HighlightRepository {

    fun list(): Observable<List<String>>

    fun configuration(): Observable<HighlightConfiguration>

    fun add(value: String): Completable

    fun remove(value: String): Completable

    fun replace(list: List<HighlightRule>): Completable
}
