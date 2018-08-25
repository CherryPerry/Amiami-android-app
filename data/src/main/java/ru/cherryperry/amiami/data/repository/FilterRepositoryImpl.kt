package ru.cherryperry.amiami.data.repository

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.functions.Function3
import ru.cherryperry.amiami.data.prefs.AppPrefs
import ru.cherryperry.amiami.domain.model.Filter
import ru.cherryperry.amiami.domain.repository.FilterRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FilterRepositoryImpl @Inject constructor(
    private val appPrefs: AppPrefs
) : FilterRepository {

    override fun filter(): Flowable<Filter> = Flowable.combineLatest(
        appPrefs.priceMin.observer,
        appPrefs.priceMax.observer,
        appPrefs.searchTerm.observer,
        Function3 { min, max, term -> Filter(min, max, term) })

    override fun setMin(value: Int): Completable = Completable.fromAction {
        if (appPrefs.priceMax.value < value) {
            throw IllegalArgumentException("Min value is lower than max")
        }
        if (appPrefs.priceMin.value != value) {
            appPrefs.priceMin.value = value
        }
    }

    override fun setMax(value: Int): Completable = Completable.fromAction {
        if (appPrefs.priceMin.value > value) {
            throw IllegalArgumentException("Max value is higher than max")
        }
        if (appPrefs.priceMax.value != value) {
            appPrefs.priceMax.value = value
        }
    }

    override fun setTerm(value: String): Completable = Completable.fromAction {
        if (appPrefs.searchTerm.value != value) {
            appPrefs.searchTerm.value = value
        }
    }
}
