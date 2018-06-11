package ru.cherryperry.amiami.data.network.server

import ru.cherryperry.amiami.model.Item
import java.util.Locale
import kotlin.collections.HashMap

class ExchangeRate(
        map: MutableMap<String, Double>
) {
    companion object {
        const val DEFAULT = "JPY"
    }

    val rates: MutableMap<String, Double> = HashMap(map)

    init {
        rates[DEFAULT] = 1.0
    }

    fun changeItemPrice(item: Item, target: String): Item {
        val rate: Double = rates[target] ?: return item
        val p = item.getPriceInt() ?: return item
        val result = rate * p
        if (DEFAULT == target) {
            item.price = String.format(Locale.getDefault(), "%.0f %s", result, target)
        } else {
            item.price = String.format(Locale.getDefault(), "%.2f %s", result, target)
        }
        return item
    }
}