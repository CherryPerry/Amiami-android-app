package ru.cherryperry.amiami.data.repository

import ru.cherryperry.amiami.data.prefs.AppPrefs
import ru.cherryperry.amiami.domain.model.HighlightConfiguration
import ru.cherryperry.amiami.domain.model.HighlightRule
import ru.cherryperry.amiami.domain.repository.HighlightRepository
import rx.Completable
import rx.Observable
import rx.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HighlightRepositoryImpl @Inject constructor(
    private val appPrefs: AppPrefs
) : HighlightRepository {

    private val scheduler = Schedulers.computation()

    override fun list(): Observable<List<String>> = appPrefs.favoriteList.observer.map { it.toList() }

    override fun configuration(): Observable<HighlightConfiguration> =
        Observable.combineLatest(
            list().map { it.map { HighlightRule(it) } },
            appPrefs.favoritesOnly.observer
        ) { list, only -> HighlightConfiguration(list, only) }

    override fun add(value: String): Completable =
        Completable.fromAction {
            val list = appPrefs.favoriteList.value.toMutableSet()
            list += value
            appPrefs.favoriteList.value = list
        }.subscribeOn(scheduler)

    override fun remove(value: String): Completable =
        Completable.fromAction {
            val list = appPrefs.favoriteList.value.toMutableSet()
            list -= value
            appPrefs.favoriteList.value = list
        }.subscribeOn(scheduler)

    override fun replace(list: List<HighlightRule>): Completable =
        Completable.fromAction {
            appPrefs.favoriteList.value = list.map { it.rule }.toSet()
        }.subscribeOn(scheduler)
}
