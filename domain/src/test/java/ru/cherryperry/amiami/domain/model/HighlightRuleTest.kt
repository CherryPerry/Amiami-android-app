package ru.cherryperry.amiami.domain.model

import org.junit.Assert
import org.junit.Test

class HighlightRuleTest {

    @Test
    fun testSimpleText() {
        val rule = HighlightRule(0, "test", false)
        Assert.assertTrue(rule.isItemHighlighted("test test"))
        Assert.assertTrue(rule.isItemHighlighted("test"))
        Assert.assertFalse(rule.isItemHighlighted("tes"))
    }

    @Test
    fun testSimpleTextIgnoreCase() {
        val rule = HighlightRule(0, "Test", false)
        Assert.assertTrue(rule.isItemHighlighted("test Test"))
        Assert.assertTrue(rule.isItemHighlighted("1Test"))
        Assert.assertFalse(rule.isItemHighlighted("tes"))
    }

    @Test
    fun testSimpleTextNotRegex() {
        val rule = HighlightRule(0, """\d+\.""", true)
        Assert.assertTrue(rule.isItemHighlighted("""a\d+\.\."""))
        Assert.assertFalse(rule.isItemHighlighted("12.test"))
    }

    @Test
    fun testRegex() {
        val rule = HighlightRule(0, """\d+\.""", true)
        Assert.assertTrue(rule.isItemHighlighted("12.test"))
        Assert.assertTrue(rule.isItemHighlighted("12."))
        Assert.assertFalse(rule.isItemHighlighted("test"))
    }
}
