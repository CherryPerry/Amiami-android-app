package ru.cherryperry.amiami.domain.model

data class Filter(
    val minPrice: Int = 0,
    val maxPrice: Int = Int.MAX_VALUE,
    val textFilter: String = ""
) {

    companion object {
        const val LIMIT = 30000
    }

    val skipAll = minPrice == 0 && maxPrice == Int.MAX_VALUE && textFilter.isEmpty()

    init {
        if (minPrice > maxPrice) {
            throw IllegalArgumentException("Invalid filter")
        }
    }

    fun isItemValid(item: Item) = skipAll || item.price.value >= minPrice && item.price.value <= maxPrice &&
        (textFilter.isEmpty() || item.name.contains(other = textFilter, ignoreCase = true))
}
