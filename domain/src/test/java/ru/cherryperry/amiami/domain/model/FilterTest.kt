package ru.cherryperry.amiami.domain.model

import org.junit.Assert
import org.junit.Test

class FilterTest {

    @Test
    fun testSkipAll() {
        Assert.assertTrue(Filter().skipAll)
        Assert.assertFalse(Filter(minPrice = 1).skipAll)
        Assert.assertFalse(Filter(maxPrice = 100).skipAll)
        Assert.assertFalse(Filter(textFilter = "test").skipAll)
    }

    @Test
    fun testMinPriceFilter() {
        val filter = Filter(minPrice = 1)
        Assert.assertTrue(filter.isItemValid(testItem(price = 2.0)))
        Assert.assertFalse(filter.isItemValid(testItem(price = 0.0)))
    }

    @Test
    fun testMaxPriceFilter() {
        val filter = Filter(maxPrice = 1)
        Assert.assertTrue(filter.isItemValid(testItem(price = 0.0)))
        Assert.assertFalse(filter.isItemValid(testItem(price = 2.0)))
    }

    @Test
    fun testTextFilter() {
        val filter = Filter(textFilter = "aa")
        Assert.assertTrue(filter.isItemValid(testItem(name = "aaa")))
        Assert.assertFalse(filter.isItemValid(testItem(name = "bbb")))
    }

    @Test
    fun testMultipleFilter() {
        val item = testItem("aaa", 15.0)
        Assert.assertTrue(Filter(10, 20, "aa").isItemValid(item))
        Assert.assertFalse(Filter(19, 20, "aa").isItemValid(item))
        Assert.assertFalse(Filter(10, 11, "aa").isItemValid(item))
        Assert.assertFalse(Filter(10, 20, "bb").isItemValid(item))
    }

    private fun testItem(
        name: String = "",
        price: Double = 0.0
    ) = Item("", "", name, Price(price), "", 0)
}
