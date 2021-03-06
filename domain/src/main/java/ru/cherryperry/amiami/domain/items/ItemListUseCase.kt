package ru.cherryperry.amiami.domain.items

import io.reactivex.Flowable
import io.reactivex.functions.Function5
import io.reactivex.schedulers.Schedulers
import ru.cherryperry.amiami.domain.FlowableUseCase
import ru.cherryperry.amiami.domain.model.ExchangeRates
import ru.cherryperry.amiami.domain.model.Filter
import ru.cherryperry.amiami.domain.model.HighlightConfiguration
import ru.cherryperry.amiami.domain.model.Item
import ru.cherryperry.amiami.domain.model.applyCurrencyChange
import ru.cherryperry.amiami.domain.model.applyHighlightConfiguration
import ru.cherryperry.amiami.domain.model.sortAndInsertGroups
import ru.cherryperry.amiami.domain.performance.DomainPerformanceUtils
import ru.cherryperry.amiami.domain.performance.PerformanceTracer
import ru.cherryperry.amiami.domain.repository.CurrencyRepository
import ru.cherryperry.amiami.domain.repository.FilterRepository
import ru.cherryperry.amiami.domain.repository.HighlightRepository
import ru.cherryperry.amiami.domain.repository.ItemRepository
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ItemListUseCase @Inject constructor(
    private val itemRepository: ItemRepository,
    private val currencyRepository: CurrencyRepository,
    private val filterRepository: FilterRepository,
    private val highlightRepository: HighlightRepository,
    private val performanceTracer: PerformanceTracer
) : FlowableUseCase<Unit, ItemListResult>() {

    companion object {
        private const val DEBOUNCE_TIME = 300L
    }

    override fun run(param: Unit): Flowable<ItemListResult> = Flowable
        .combineLatest(
            itemRepository.items().toFlowable(),
            currencyRepository.currency().toFlowable(),
            currencyRepository.selectedCurrency(),
            filterRepository.filter(),
            highlightRepository.configuration(),
            Function5<List<Item>, ExchangeRates, String, Filter, HighlightConfiguration, Data>
            { list, currency, selectedCurrency, filter, highlights ->
                Data(list, currency, selectedCurrency, filter, highlights)
            })
        .debounce(DEBOUNCE_TIME, TimeUnit.MILLISECONDS, Schedulers.computation())
        .map { data ->
            performanceTracer.startTrace(DomainPerformanceUtils.ITEM_LIST_FILTER_TRACE,
                DomainPerformanceUtils.attributesItemListUseCase(data)).use {
                val filteredList = data.list
                    .asSequence()
                    .filter { item -> data.filter.isItemValid(item) }
                    .applyHighlightConfiguration(data.highlight)
                    .applyCurrencyChange(data.exchangeRate, data.selectedCurrency)
                    .sortAndInsertGroups()
                    .toList()
                ItemListResult(filteredList, !data.filter.skipAll)
            }
        }

    class Data(
        val list: List<Item>,
        val exchangeRate: ExchangeRates,
        val selectedCurrency: String,
        val filter: Filter,
        val highlight: HighlightConfiguration
    )
}
