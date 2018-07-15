package ru.cherryperry.amiami.screen.activity

import android.support.annotation.IdRes
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.widget.Toolbar
import ru.cherryperry.amiami.R
import ru.cherryperry.amiami.domain.update.UpdateInfo
import ru.cherryperry.amiami.screen.highlight.HighlightFragment
import ru.cherryperry.amiami.screen.main.MainFragment
import ru.cherryperry.amiami.screen.settings.SettingsFragment
import ru.cherryperry.amiami.screen.update.UpdateDialogFragment

class Navigator(
    private val fragmentManager: FragmentManager,
    @IdRes private val rootView: Int
) {

    fun openList() {
        fragmentManager.beginTransaction()
            .replace(rootView, MainFragment())
            .commitAllowingStateLoss()
    }

    fun openSettings() {
        fragmentManager.beginTransaction()
            .replace(rootView, SettingsFragment())
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .addToBackStack(null)
            .commitAllowingStateLoss()
    }

    fun openHighlight() {
        fragmentManager.beginTransaction()
            .replace(rootView, HighlightFragment())
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .addToBackStack(null)
            .commitAllowingStateLoss()
    }

    fun openItem() {
        // TODO
    }

    fun openUpdateDialog(updateInfo: UpdateInfo) {
        UpdateDialogFragment.newInstance(updateInfo)
            .show(fragmentManager, UpdateDialogFragment::class.java.simpleName)
    }

    fun canGoBack() = fragmentManager.backStackEntryCount > 0

    fun back() {
        if (canGoBack()) {
            fragmentManager.popBackStackImmediate()
        }
    }

    fun configureToolbar(toolbar: Toolbar) {
        if (canGoBack()) {
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp)
            toolbar.setNavigationOnClickListener { back() }
        }
    }
}