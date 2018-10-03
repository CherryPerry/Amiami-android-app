package ru.cherryperry.amiami.domain.performance

import ru.cherryperry.amiami.domain.items.ItemListUseCase

object DomainPerformanceUtils {

    const val ITEM_LIST_FILTER_TRACE = "ItemListFilterTrace"

    fun attributesItemListUseCase(data: ItemListUseCase.Data): Map<String, String> =
        HashMap<String, String>().apply {
            put("items_count", data.list.size.toString())
            put("selected_currency", data.selectedCurrency)
            put("filter", (!data.filter.skipAll).toString())
            put("highlight_count", data.highlight.rules.size.toString())
            put("highlight_filter", data.highlight.asFilter.toString())
        }
}
