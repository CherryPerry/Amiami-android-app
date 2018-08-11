package ru.cherryperry.amiami.data.mapping

import org.junit.Assert
import org.junit.Test

class JsonPriceToPriceMappingTest {

    companion object {
        private const val DELTA = 0.01
    }

    private val jsonPriceToPriceMapping = JsonPriceToPriceMapping()

    @Test
    fun testEmpty() {
        Assert.assertEquals(0.0, jsonPriceToPriceMapping.map("").value, DELTA)
    }

    @Test
    fun testInvalid() {
        Assert.assertEquals(0.0, jsonPriceToPriceMapping.map("not a price at all!").value, DELTA)
    }

    @Test
    fun testNormal() {
        Assert.assertEquals(2180.0, jsonPriceToPriceMapping.map("2,180 JPY").value, DELTA)
    }

    @Test
    fun testNormalWithRangeMark() {
        Assert.assertEquals(5780.0, jsonPriceToPriceMapping.map("5,780 JPY ~").value, DELTA)
    }

    @Test
    fun testLowPrice() {
        Assert.assertEquals(560.0, jsonPriceToPriceMapping.map("560 JPY ~").value, DELTA)
    }
}
