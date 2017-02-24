package ru.cherryperry.amiami.model

import java.util.*

class Items {
    /**
     * Map Time -> Items (with same time)
     */
    private val groups: MutableMap<Long, ItemGroup> = TreeMap(Collections.reverseOrder<Long>())

    /**
     * Sorted list for adapter
     */
    private val positions: MutableList<Position> = ArrayList(2048)

    // Filter by price at add
    private var filterPriceMax = Integer.MAX_VALUE
    private var filterPriceMin = 0
    private var searchTerm: String = ""

    fun size(): Int {
        return positions.size
    }

    fun add(item: Item) {
        if (!isValidForFilter(item)) return
        if (!groups.containsKey(item.time)) groups.put(item.time, ItemGroup(item.time))
        groups[item.time]?.items?.add(item)
    }

    fun isValidForFilter(item: Item): Boolean {
        return item.isPriceInRange(filterPriceMin, filterPriceMax) && item.isContainsTerm(searchTerm)
    }

    fun calculate() {
        positions.clear()

        // Сортируем каждую группу
        groups.values.forEach { it.sort() }

        var index = 0
        for ((key, value) in groups)
            if (value.items.size > 0) {
                positions.add(Position(value.time, null, index++))
                value.items.mapTo(positions) { Position(value.time, it, index++) }
            }
    }

    fun getItem(position: Int): Position {
        return positions[position]
    }

    fun setFilter(min: Int, max: Int, term: String) {
        filterPriceMin = min
        filterPriceMax = max
        searchTerm = term
    }

    fun getFilterMin() = filterPriceMin

    fun getFilterMax() = filterPriceMax

    fun getSearchTerm() = searchTerm

    val isFilterEnabled: Boolean
        get() = filterPriceMin != 0 || filterPriceMax != Integer.MAX_VALUE || searchTerm.isNotEmpty()

    class Position(val time: Long, val item: Item?, val position: Int)
}
