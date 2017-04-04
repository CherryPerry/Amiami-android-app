package ru.cherryperry.amiami.screen.highlight

import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import ru.cherryperry.amiami.AmiamiApplication

@RunWith(RobolectricTestRunner::class)
@Config(manifest = "src/main/AndroidManifest.xml", application = AmiamiApplication::class, sdk = intArrayOf(21))
class HighlightPresenterTest {
    lateinit var highlightPresenter: HighlightPresenter

    @Before
    fun before() {
        highlightPresenter = HighlightPresenter()
    }

    @Test
    fun testAdd() {
        var count = 0
        val view = object : HighlightViewImpl() {
            override fun showData(itemList: List<String>) {
                count = itemList.size
            }
        }
        highlightPresenter.attachView(view)
        val lastCount = count
        highlightPresenter.addItem("1")
        Assert.assertEquals("Item was not add!", lastCount + 1, count)
    }

    @Test
    fun testRemove() {
        val item = "1"
        highlightPresenter.addItem(item)

        var count = 0
        var index = 0
        val view = object : HighlightViewImpl() {
            override fun showData(itemList: List<String>) {
                count = itemList.size
                index = itemList.indexOf(item)
            }
        }
        highlightPresenter.attachView(view)
        val lastCount = count
        // TODO
        // highlightPresenter.deleteItem(index)
        Assert.assertEquals("Item was not removed!", lastCount - 1, count)
    }

    @After
    fun after() {
        highlightPresenter.onDestroy()
    }

    open class HighlightViewImpl : HighlightView {
        override fun showData(itemList: List<String>) {
        }
    }
}