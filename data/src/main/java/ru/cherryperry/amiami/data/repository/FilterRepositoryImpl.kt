package ru.cherryperry.amiami.data.repository

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Scheduler
import io.reactivex.functions.Function3
import ru.cherryperry.amiami.core.createBackgroundThreadScheduler
import ru.cherryperry.amiami.data.R
import ru.cherryperry.amiami.data.prefs.IntPreference
import ru.cherryperry.amiami.data.prefs.StringPreference
import ru.cherryperry.amiami.domain.model.Filter
import ru.cherryperry.amiami.domain.repository.FilterRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FilterRepositoryImpl constructor(
    context: Context,
    private val scheduler: Scheduler
) : FilterRepository {

    @Inject
    constructor(context: Context) : this(context, createBackgroundThreadScheduler("FilterRepositoryThread"))

    private val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    private val priceMin = IntPreference(
        context.getString(R.string.key_price_min), 0, preferences, scheduler)
    private val priceMax = IntPreference(
        context.getString(R.string.key_price_max), Int.MAX_VALUE, preferences, scheduler)
    private val searchTerm = StringPreference(
        context.getString(R.string.key_search_filter), "", preferences, scheduler)

    override fun filter(): Flowable<Filter> = Flowable
        .combineLatest(
            priceMin.observer,
            priceMax.observer,
            searchTerm.observer,
            Function3<Int, Int, String, Triple<Int, Int, String>> { min, max, term -> Triple(min, max, term) })
        .flatMapMaybe {
            // possible race condition on observers, so min can be less than max
            try {
                Maybe.just(Filter(it.first, it.second, it.third))
            } catch (exception: IllegalArgumentException) {
                Maybe.never<Filter>()
            }
        }

    override fun setMin(value: Int): Completable = Completable
        .fromAction {
            if (priceMax.value < value) {
                priceMax.value = value
            }
            if (priceMin.value != value) {
                priceMin.value = value
            }
        }
        .subscribeOn(scheduler)

    override fun setMax(value: Int): Completable = Completable
        .fromAction {
            if (priceMin.value > value) {
                priceMin.value = value
            }
            if (priceMax.value != value) {
                priceMax.value = value
            }
        }
        .subscribeOn(scheduler)

    override fun setTerm(value: String): Completable = Completable
        .fromAction {
            if (searchTerm.value != value) {
                searchTerm.value = value
            }
        }
        .subscribeOn(scheduler)
}
