package ru.cherryperry.amiami.domain.model

import org.junit.Assert
import org.junit.Test

class ExchangeRatesTest {

    // DEFAULT should be set to 1.0
    private val map = mapOf(ExchangeRates.DEFAULT to 2.0, "USD" to 2.0, "EUR" to 3.0)
    private val rates = ExchangeRates(map)

    @Test
    fun testChangePriceDefault() {
        val item = createItem(Price(1.0))
        val changed = rates.changePrice(item, ExchangeRates.DEFAULT)
        Assert.assertEquals(item, changed)
    }

    @Test
    fun testChangePriceNotFound() {
        val item = createItem(Price(1.0))
        val changed = rates.changePrice(item, "RUB")
        Assert.assertEquals(item, changed)
    }

    @Test
    fun testChangePrice() {
        val item = createItem(Price(1.0))
        val changed = rates.changePrice(item, "USD")
        Assert.assertEquals(item.copy(price = Price(2.0, "USD")), changed)
    }

    private fun createItem(price: Price) = Item("url", "image", "name", price, "discount", 0)
}
