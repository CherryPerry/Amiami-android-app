package ru.cherryperry.amiami.presentation.activity

import android.net.Uri
import android.webkit.URLUtil
import androidx.annotation.IdRes
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import ru.cherryperry.amiami.R
import ru.cherryperry.amiami.data.prefs.AppPrefs
import ru.cherryperry.amiami.domain.model.Item
import ru.cherryperry.amiami.domain.model.UpdateInfo
import ru.cherryperry.amiami.presentation.highlight.HighlightFragment
import ru.cherryperry.amiami.presentation.main.MainFragment
import ru.cherryperry.amiami.presentation.settings.SettingsFragment
import ru.cherryperry.amiami.presentation.update.UpdateDialogFragment

class Navigator(
    private val fragmentManager: FragmentManager,
    @IdRes private val rootView: Int,
    private val singleActivity: SingleActivity,
    // TODO Replace with domain repository!
    private val appPrefs: AppPrefs
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

    fun openItem(item: Item) {
        if (!URLUtil.isNetworkUrl(item.url)) {
            return
        }
        val uri = Uri.parse(item.url)
        if (!(appPrefs.chromeCustomTabs.value && singleActivity.tryOpenCustomTabs(uri))) {
            singleActivity.openDefault(uri)
        }
    }

    fun openUpdateDialog(updateInfo: UpdateInfo) {
        UpdateDialogFragment.newInstance(updateInfo)
            .show(fragmentManager, UpdateDialogFragment::class.java.simpleName)
    }

    fun openUrl(url: String) {
        if (!URLUtil.isNetworkUrl(url)) {
            return
        }
        val uri = Uri.parse(url)
        if (!(appPrefs.chromeCustomTabs.value && singleActivity.tryOpenCustomTabs(uri))) {
            singleActivity.openDefault(uri)
        }
    }

    fun canGoBack() = fragmentManager.backStackEntryCount > 0

    fun back() {
        if (canGoBack()) {
            fragmentManager.popBackStackImmediate()
        }
    }

    fun configureToolbar(toolbar: Toolbar) {
        if (canGoBack()) {
            toolbar.setNavigationIcon(R.drawable.icon_back_24dp_black)
            toolbar.setNavigationOnClickListener { back() }
        }
    }
}
