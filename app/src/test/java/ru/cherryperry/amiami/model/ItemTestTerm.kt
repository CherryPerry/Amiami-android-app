package ru.cherryperry.amiami.model

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ItemTestTerm {

    @Test
    fun testItemTermNull() {
        val item = Item()
        item.name = null
        assertFalse(item.isContainsTerm("term"))
    }

    @Test
    fun testItemTermEmpty() {
        val item = Item()
        item.name = ""
        assertFalse(item.isContainsTerm("term"))
    }

    @Test
    fun testItemTermEmptySearchEmpty() {
        val item = Item()
        item.name = ""
        assertTrue(item.isContainsTerm(""))
    }

    @Test
    fun testItemTermEmptySearchNull() {
        val item = Item()
        item.name = ""
        assertTrue(item.isContainsTerm(null))
    }

    @Test
    fun testItemTermNullSearchNull() {
        val item = Item()
        item.name = null
        assertTrue(item.isContainsTerm(null))
    }

    @Test
    fun testItemTermContains() {
        val item = Item()
        item.name = "term term"
        assertTrue(item.isContainsTerm("term"))
    }

    @Test
    fun testItemTermNotContains() {
        val item = Item()
        item.name = "nothing"
        assertFalse(item.isContainsTerm("term"))
    }

    @Test
    fun testItemTermIgnoreCaseContains() {
        val item = Item()
        item.name = "TERM TERM"
        assertTrue(item.isContainsTerm("term"))
    }
}