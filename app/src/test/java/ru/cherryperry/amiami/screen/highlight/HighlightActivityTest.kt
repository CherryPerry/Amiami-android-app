package ru.cherryperry.amiami.screen.highlight

import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class HighlightActivityTest {

    @Test
    fun test() {
        val activity = Robolectric.buildActivity(HighlightActivity::class.java)
    }
}