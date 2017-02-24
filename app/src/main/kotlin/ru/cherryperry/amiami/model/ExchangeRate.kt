package ru.cherryperry.amiami.model

import org.json.JSONObject
import java.util.*

class ExchangeRate(json: String) {
    companion object {
        val DEFAULT = "JPY"
    }

    val rates: MutableMap<String, Double> = TreeMap()

    init {
        val ratesJson = JSONObject(json).getJSONObject("rates")
        ratesJson.keys().forEach {
            rates.put(it, ratesJson.getDouble(it))
        }
        rates.put(DEFAULT, 1.0)
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