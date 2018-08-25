package ru.cherryperry.amiami.domain.model

import org.junit.Assert
import org.junit.Test

class HighlightRuleTest {

    @Test
    fun testSimpleText() {
        val rule = HighlightRule(0, "test", false)
        Assert.assertTrue(rule.isItemHighlighted("test test"))
        Assert.assertTrue(rule.isItemHighlighted("Test"))
        Assert.assertFalse(rule.isItemHighlighted("tes"))
    }

    @Test
    fun testSimpleTextLikeRegex() {
        val rule = HighlightRule(0, """\d+\.""", false)
        Assert.assertTrue(rule.isItemHighlighted("""a\d+\.\."""))
        Assert.assertFalse(rule.isItemHighlighted("12.test"))
    }

    @Test
    fun testValidRegex() {
        val rule = HighlightRule(0, """\d+\.""", true)
        Assert.assertTrue(rule.isItemHighlighted("12.test"))
        Assert.assertTrue(rule.isItemHighlighted("12."))
        Assert.assertFalse(rule.isItemHighlighted("test"))
    }

    @Test
    fun testInvalidRegex() {
        val rule = HighlightRule(0, """(\d+\.""", true)
        Assert.assertFalse(rule.isItemHighlighted("12.test"))
        Assert.assertFalse(rule.isItemHighlighted("12."))
        Assert.assertFalse(rule.isItemHighlighted("test"))
    }
}
