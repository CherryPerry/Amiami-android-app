package ru.cherryperry.amiami.domain.model

import org.junit.Assert
import org.junit.Test

class HighlightRuleTest {

    @Test
    fun testSimpleText() {
        val rule = HighlightRule("test")
        Assert.assertTrue(rule.isItemHighlighted("test test"))
        Assert.assertTrue(rule.isItemHighlighted("test"))
        Assert.assertFalse(rule.isItemHighlighted("tes"))
    }

    @Test
    fun testRegex() {
        val rule = HighlightRule("""\d+\.""")
        Assert.assertTrue(rule.isItemHighlighted("12.test"))
        Assert.assertTrue(rule.isItemHighlighted("12."))
        Assert.assertFalse(rule.isItemHighlighted("test"))
    }
}
