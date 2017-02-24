package ru.cherryperry.amiami.model

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ItemTestRange {

    @Test
    fun testItemRangeValidComma() {
        val item = Item()
        item.price = "3,400 JPY"
        fullTest(item, 3400)
    }

    @Test
    fun testItemRangeValid() {
        val item = Item()
        item.price = "340 JPY"
        fullTest(item, 340)
    }

    @Test
    fun testItemRangeValidTildaComma() {
        val item = Item()
        item.price = "3,400 JPY ~"
        fullTest(item, 3400)
    }

    @Test
    fun testItemRangeValidTilda() {
        val item = Item()
        item.price = "340 JPY ~"
        fullTest(item, 340)
    }

    @Test
    fun testItemRangeValidEmpty() {
        val item = Item()
        item.price = ""

        assertFalse(item.isPriceInRange(0, Integer.MAX_VALUE))
    }

    @Test
    fun testItemRangeValidNull() {
        val item = Item()
        item.price = null

        assertFalse(item.isPriceInRange(0, Integer.MAX_VALUE))
    }

    @Test
    fun testItemRangeValidInvalidString() {
        val item = Item()
        item.price = "abc abc"

        assertFalse(item.isPriceInRange(0, Integer.MAX_VALUE))
    }

    private fun fullTest(item: Item, value: Int) {
        assertTrue("Global range test", item.isPriceInRange(0, Integer.MAX_VALUE))
        assertTrue("Min range test", item.isPriceInRange(value, Integer.MAX_VALUE))
        assertTrue("Max range test", item.isPriceInRange(0, value))
        assertFalse("Not in range test", item.isPriceInRange(Integer.MAX_VALUE - 1, Integer.MAX_VALUE))
    }
}
