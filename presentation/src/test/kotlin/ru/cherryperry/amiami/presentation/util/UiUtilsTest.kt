package ru.cherryperry.amiami.presentation.util

import android.graphics.Rect
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.view.View
import android.view.WindowInsets
import android.widget.EditText
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class UiUtilsTest {

    @Test
    fun testAddPaddingBottomToFitBottomSystemInset() {
        val activity = Robolectric.buildActivity(AppCompatActivity::class.java)
        val view = View(activity.get())
        view.setPadding(10, 20, 30, 40)
        addPaddingBottomToFitBottomSystemInset(view)
        // thanks google
        val insets = WindowInsets::class.java.constructors
            .first { it.parameterCount == 5 }
            .newInstance(Rect(10, 10, 10, 100), null, null, false, false)
        val insetsCompat = WindowInsetsCompat::class.java.constructors
            .first { it.parameterCount == 1 && it.parameterTypes[0] == Object::class.java }
            .newInstance(insets) as WindowInsetsCompat
        ViewCompat.dispatchApplyWindowInsets(view, insetsCompat)
        Assert.assertEquals(10, view.paddingLeft)
        Assert.assertEquals(20, view.paddingTop)
        Assert.assertEquals(30, view.paddingRight)
        Assert.assertEquals(140, view.paddingBottom)
    }

    @Test
    fun testAfterTextChanged() {
        val activity = Robolectric.buildActivity(AppCompatActivity::class.java)
        val editText = EditText(activity.get())
        var blockCalled = 0
        val block: (Editable) -> Unit = {
            Assert.assertEquals("test", it.toString())
            blockCalled++
        }
        editText.afterTextChanged(block)
        editText.setText("test")
        Assert.assertEquals(1, blockCalled)
    }
}
