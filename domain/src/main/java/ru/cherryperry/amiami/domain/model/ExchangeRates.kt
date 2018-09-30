package ru.cherryperry.amiami.domain.model

class ExchangeRates(
    rates: Map<String, Double>
) {

    companion object {
        const val DEFAULT = "JPY"
    }

    val rates: Map<String, Double>

    val currencies
        get() = rates.keys

    init {
        val newRates = HashMap<String, Double>()
        newRates += rates
        newRates[DEFAULT] = 1.0
        this.rates = newRates
    }

    // TODO Check price if it DEFAULT else throw
    fun changePrice(item: Item, target: String): Item =
        if (target == DEFAULT || !rates.containsKey(target)) {
            item
        } else {
            val newPrice = rates[target]!! * item.price.value
            item.copy(price = Price(newPrice, target))
        }
}
