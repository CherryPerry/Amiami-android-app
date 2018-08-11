package ru.cherryperry.amiami.domain.model

import org.junit.Assert
import org.junit.Test

class GroupItemExtensionsTest {

    @Test
    fun testEmpty() {
        val list = emptySequence<Item>()
            .sortAndInsertGroups()
            .toList()
        Assert.assertTrue(list.isEmpty())
    }

    @Test
    fun testSingleItem() {
        val list = sequenceOf(create(1))
            .sortAndInsertGroups()
            .toList()
        Assert.assertEquals(1L, (list[0] as Group).time)
        Assert.assertEquals(1L, (list[1] as Item).time)
    }

    @Test
    fun testMultipleItems() {
        val list = sequenceOf(create(1), create(0), create(1))
            .sortAndInsertGroups()
            .toList()
        Assert.assertEquals(1L, (list[0] as Group).time)
        Assert.assertEquals(1L, (list[1] as Item).time)
        Assert.assertEquals(1L, (list[2] as Item).time)
        Assert.assertEquals(0L, (list[3] as Group).time)
        Assert.assertEquals(0L, (list[4] as Item).time)
    }

    private fun create(time: Long) = Item("", "", "", Price(), "", time)
}
