package ru.cherryperry.amiami.model

import ru.cherryperry.amiami.AppPrefs
import ru.cherryperry.amiami.network.API
import ru.cherryperry.amiami.network.ApiProvider
import rx.Observable
import rx.schedulers.Schedulers
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ItemRepository @Inject constructor(private val api: ApiProvider,
                                         private val appPrefs: AppPrefs,
                                         private val currencyRepository: CurrencyRepository) {
    private val cache = ArrayList<Item>()

    fun items(forceNetwork: Boolean): Observable<Items> {
        // val items = Items()
        // items.setFilter(appPrefs.priceMin, appPrefs.priceMax, appPrefs.searchTerm)

        if (forceNetwork || cache.isEmpty())
            return api.api()
                    .flatMap(API::items)
                    .map { it.map(::Item) }
                    .doOnNext {
                        cache.clear()
                        cache.addAll(it)
                    }
                    .compose { applyFilter(it) }
                    .flatMap({ currencyRepository.exchangeRate(forceNetwork) }, ::ItemsWithExchange)
                    .compose { applyExchangeRate(it) }
                    .map {
                        val items = Items()
                        it.forEach { items.add(it) }

                        // setFilter для отображения в UI фильтров
                        items.setFilter(appPrefs.priceMin, appPrefs.priceMax, appPrefs.searchTerm)

                        items.calculate()
                        items
                    }
        else
            return Observable.just(cache as List<Item>)
                    .compose { applyFilter(it) }
                    .flatMap({ currencyRepository.exchangeRate(forceNetwork) }, ::ItemsWithExchange)
                    .compose { applyExchangeRate(it) }
                    .map {
                        val items = Items()
                        it.forEach { items.add(it) }

                        // setFilter для отображения в UI фильтров
                        items.setFilter(appPrefs.priceMin, appPrefs.priceMax, appPrefs.searchTerm)

                        items.calculate()
                        items
                    }
    }

    /**
     * Обмен валюты списка
     */
    private fun applyExchangeRate(observable: Observable<ItemsWithExchange>): Observable<List<Item>> = observable.flatMap {
        if (it.rates != null) {
            val rates = it.rates
            Observable.from(it.items)
                    .flatMap {
                        Observable.just(it)
                                .subscribeOn(Schedulers.computation())
                                .map { rates.changeItemPrice(it.copy(), appPrefs.exchangeCurrency) }
                    }
                    .toList()
        } else Observable.just(it.items)
    }

    /**
     * Фильтрация списка
     */
    private fun applyFilter(observable: Observable<List<Item>>): Observable<List<Item>> = observable.map {
        // TODO Different class
        val items = Items()
        items.setFilter(appPrefs.priceMin, appPrefs.priceMax, appPrefs.searchTerm)

        val filtered = ArrayList<Item>(it.size)
        it.forEach { if (items.isValidForFilter(it)) filtered.add(it) }
        filtered
    }

    private data class ItemsWithExchange(val items: List<Item>, val rates: ExchangeRate?)
}