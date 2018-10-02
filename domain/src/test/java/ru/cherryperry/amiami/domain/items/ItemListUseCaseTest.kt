package ru.cherryperry.amiami.domain.items

import io.reactivex.Flowable
import io.reactivex.Single
import org.junit.Test
import org.mockito.Mockito
import ru.cherryperry.amiami.domain.model.ExchangeRates
import ru.cherryperry.amiami.domain.model.Filter
import ru.cherryperry.amiami.domain.model.Group
import ru.cherryperry.amiami.domain.model.HighlightConfiguration
import ru.cherryperry.amiami.domain.model.HighlightRule
import ru.cherryperry.amiami.domain.model.Item
import ru.cherryperry.amiami.domain.model.Price
import ru.cherryperry.amiami.domain.repository.CurrencyRepository
import ru.cherryperry.amiami.domain.repository.FilterRepository
import ru.cherryperry.amiami.domain.repository.HighlightRepository
import ru.cherryperry.amiami.domain.repository.ItemRepository

class ItemListUseCaseTest {

    @Test
    fun testEmpty() {
        val itemRepository = Mockito.mock(ItemRepository::class.java)
        Mockito.`when`(itemRepository.items()).thenReturn(Single.just(emptyList()))
        val currencyRepository = Mockito.mock(CurrencyRepository::class.java)
        Mockito.`when`(currencyRepository.currency()).thenReturn(Single.just(ExchangeRates()))
        Mockito.`when`(currencyRepository.selectedCurrency()).thenReturn(Flowable.just(ExchangeRates.DEFAULT))
        val filterRepository = Mockito.mock(FilterRepository::class.java)
        Mockito.`when`(filterRepository.filter()).thenReturn(Flowable.just(Filter()))
        val highlightRepository = Mockito.mock(HighlightRepository::class.java)
        Mockito.`when`(highlightRepository.configuration()).thenReturn(Flowable.just(HighlightConfiguration()))
        ItemListUseCase(itemRepository, currencyRepository, filterRepository, highlightRepository)
            .run(Unit)
            .test()
            .awaitCount(1)
            .assertValue(ItemListResult())
            .dispose()
    }

    @Test
    fun testJustList() {
        // test no filters, no mappers
        val itemRepository = Mockito.mock(ItemRepository::class.java)
        val list = createDefaultItemList()
        Mockito.`when`(itemRepository.items()).thenReturn(Single.just(list))
        val currencyRepository = Mockito.mock(CurrencyRepository::class.java)
        Mockito.`when`(currencyRepository.currency()).thenReturn(Single.just(ExchangeRates()))
        Mockito.`when`(currencyRepository.selectedCurrency()).thenReturn(Flowable.just(ExchangeRates.DEFAULT))
        val filterRepository = Mockito.mock(FilterRepository::class.java)
        Mockito.`when`(filterRepository.filter()).thenReturn(Flowable.just(Filter()))
        val highlightRepository = Mockito.mock(HighlightRepository::class.java)
        Mockito.`when`(highlightRepository.configuration()).thenReturn(Flowable.just(HighlightConfiguration()))
        ItemListUseCase(itemRepository, currencyRepository, filterRepository, highlightRepository)
            .run(Unit)
            .test()
            .awaitCount(1)
            .assertValue(ItemListResult(mutableListOf(Group(1)) + list))
            .dispose()
    }

    @Test
    fun testAll() {
        // test all filters and mappers at the same time
        // list of 10 items
        val itemRepository = Mockito.mock(ItemRepository::class.java)
        val list = createDefaultItemList()
        Mockito.`when`(itemRepository.items()).thenReturn(Single.just(list))
        // convert currency to USD
        val currencyRepository = Mockito.mock(CurrencyRepository::class.java)
        Mockito.`when`(currencyRepository.currency()).thenReturn(Single.just(ExchangeRates(mapOf("USD" to 2.0))))
        Mockito.`when`(currencyRepository.selectedCurrency()).thenReturn(Flowable.just("USD"))
        // filter by min price 5
        val filterRepository = Mockito.mock(FilterRepository::class.java)
        Mockito.`when`(filterRepository.filter()).thenReturn(Flowable.just(Filter(minPrice = 5)))
        // filter by name with 5,6,7
        val highlightRepository = Mockito.mock(HighlightRepository::class.java)
        Mockito.`when`(highlightRepository.configuration()).thenReturn(
            Flowable.just(HighlightConfiguration(listOf(HighlightRule(rule = "name[567]", regex = true)))))
        // final list must contain 5,6,7
        val result = list
            .asSequence()
            .filter { it.price.value >= 5 }
            .map {
                if (it.name.contains(regex = Regex("[567]"))) {
                    it.copy(price = Price(it.price.value * 2, "USD"), highlight = true)
                } else {
                    it.copy(price = Price(it.price.value * 2, "USD"))
                }
            }
            .toList()
        // check it
        ItemListUseCase(itemRepository, currencyRepository, filterRepository, highlightRepository)
            .run(Unit)
            .test()
            .awaitCount(1)
            .assertValue(ItemListResult(mutableListOf(Group(1)) + result, true))
            .dispose()
    }

    private fun createDefaultItemList(): List<Item> {
        val list = mutableListOf<Item>()
        for (i in 0..9) {
            list.add(Item("url$i", "image$i", "name$i", Price(i.toDouble()), "discount$i", 1))
        }
        return list
    }
}
