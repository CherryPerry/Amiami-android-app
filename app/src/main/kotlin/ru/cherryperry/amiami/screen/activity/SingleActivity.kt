package ru.cherryperry.amiami.screen.activity

import android.os.Bundle
import android.widget.Toast
import dagger.android.support.DaggerAppCompatActivity
import ru.cherryperry.amiami.R
import javax.inject.Inject

class SingleActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var navigator: Navigator

    private var backButtonWasPressed = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            navigator.openList()
        }
    }

    override fun onBackPressed() {
        // at least one fragment intercepted onBackPressed action
        if (supportFragmentManager.fragments
                        .asSequence()
                        .filterIsInstance(OnBackKeyPressedListener::class.java)
                        .any { it.onBackPressed() }) {
            return
        }
        if (navigator.canGoBack()) {
            navigator.back()
        } else {
            // close app only after double back click
            if (backButtonWasPressed) {
                backButtonWasPressed = false
                super.onBackPressed()
            } else {
                backButtonWasPressed = true
                window?.decorView?.postDelayed({ backButtonWasPressed = false }, 2000)
                Toast.makeText(this, R.string.back_button_alert, Toast.LENGTH_SHORT).show()
            }
        }
    }
}