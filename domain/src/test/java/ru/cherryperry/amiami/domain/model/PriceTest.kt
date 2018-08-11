package ru.cherryperry.amiami.domain.model

import org.junit.Assert
import org.junit.Test

class PriceTest {

    @Test
    fun testToStringDefault() {
        val price = Price(1.0)
        // should be without separator
        Assert.assertTrue(price.toString().matches(Regex("\\d+ ${ExchangeRates.DEFAULT}")))
    }

    @Test
    fun testToStringOther() {
        val price = Price(1.0, "RUB")
        // should be with separator
        Assert.assertTrue(price.toString().matches(Regex("\\d+[,.]\\d+ RUB")))
    }
}
