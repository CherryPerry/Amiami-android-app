package ru.cherryperry.amiami.domain.model

import org.junit.Assert
import org.junit.Test

class VersionTest {

    @Test
    fun testOneDigitVersion() {
        Assert.assertEquals(Version(1), Version.fromString("1"))
    }

    @Test
    fun testTwoDigitVersion() {
        Assert.assertEquals(Version(1, 2), Version.fromString("1.2"))
    }

    @Test
    fun testThreeDigitVersion() {
        Assert.assertEquals(Version(1, 2, 3), Version.fromString("1.2.3"))
    }

    @Test(expected = IllegalArgumentException::class)
    fun testParseInvalidVersion() {
        Version.fromString("Invalid")
    }

    @Test
    fun testCompareOneDigitVersion() {
        Assert.assertTrue(Version(1, 2, 3) < Version(2))
    }

    @Test
    fun testCompareTwoDigitVersion() {
        Assert.assertTrue(Version(1, 2, 3) < Version(1, 3))
    }

    @Test
    fun testCompareThreeDigitVersion() {
        Assert.assertTrue(Version(1, 2, 3) < Version(1, 2, 4))
    }
}
