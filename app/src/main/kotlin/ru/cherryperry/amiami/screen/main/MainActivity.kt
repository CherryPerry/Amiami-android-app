package ru.cherryperry.amiami.screen.main

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.customtabs.CustomTabsIntent
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.CoordinatorLayout
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.DisplayMetrics
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.URLUtil
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.messaging.FirebaseMessaging
import dagger.android.support.DaggerAppCompatActivity
import org.chromium.customtabsclient.shared.CustomTabsHelper
import org.chromium.customtabsdemos.CustomTabActivityHelper
import ru.cherryperry.amiami.AppPrefs
import ru.cherryperry.amiami.R
import ru.cherryperry.amiami.model.Item
import ru.cherryperry.amiami.model.Items
import ru.cherryperry.amiami.push.MessagingService
import ru.cherryperry.amiami.push.NotificationController
import ru.cherryperry.amiami.screen.highlight.HighlightActivity
import ru.cherryperry.amiami.screen.settings.SettingsActivity
import ru.cherryperry.amiami.screen.update.UpdateDialogFragment
import ru.cherryperry.amiami.screen.update.UpdateDialogViewModel
import ru.cherryperry.amiami.util.FirebaseAnalyticsHelper
import ru.cherryperry.amiami.util.ViewDelegate
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity(), CustomTabActivityHelper.ConnectionCallback {

    companion object {
        private const val RC_SETTINGS = 704
        private const val RC_HIGHLIGHT = 507
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var mainViewModel: MainViewModel
    private lateinit var updateDialogViewModel: UpdateDialogViewModel

    // Views
    private val recyclerView by ViewDelegate<RecyclerView>(R.id.recyclerView)
    private val toolbar by ViewDelegate<Toolbar>(R.id.toolbar)
    private val swipeRefreshLayout by ViewDelegate<SwipeRefreshLayout>(R.id.swipeRefreshLayout)
    private val filterHintView by ViewDelegate<TextView>(R.id.filter)
    private val filterView by ViewDelegate<FilterView>(R.id.filterView)

    // Late init controllers
    private lateinit var itemAdapter: ItemAdapter
    private lateinit var customTabActivityHelper: CustomTabActivityHelper

    @Inject
    lateinit var prefs: AppPrefs
    @Inject
    lateinit var firebaseAnalytics: FirebaseAnalyticsHelper
    @Inject
    lateinit var notificationController: NotificationController

    // Runtime state
    private var customTabAvailable = false
    private var isPaused = true
    private var webViewPreviewDialog: WebViewPreviewDialog? = null
    private var backButtonWasPressed = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(MainViewModel::class.java)
        updateDialogViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(UpdateDialogViewModel::class.java)

        setContentView(R.layout.activity_main)

        FirebaseMessaging.getInstance().unsubscribeFromTopic("updates")
        if (prefs.push)
            FirebaseMessaging.getInstance().subscribeToTopic(MessagingService.updateTopic)
        else
            FirebaseMessaging.getInstance().unsubscribeFromTopic(MessagingService.updateTopic)

        setSupportActionBar(toolbar)

        customTabActivityHelper = CustomTabActivityHelper()
        customTabActivityHelper.setConnectionCallback(this)

        setAdapter()

        swipeRefreshLayout.setOnRefreshListener { mainViewModel.load() }

        // Filter view
        filterView.onSearchTextChange = { s ->
            firebaseAnalytics.logSearch(filterView.getMinValue(), filterView.getMaxValue(), s)
            mainViewModel.reapplyFilter()
        }
        filterView.onSeekBarChange = { min, max ->
            firebaseAnalytics.logSearch(min, max, filterView.getSearchTerm())
            mainViewModel.reapplyFilter()
        }
        filterView.onRequestShowSheet = {
            val params = filterView.layoutParams as CoordinatorLayout.LayoutParams
            (params.behavior as BottomSheetBehavior<*>).state = BottomSheetBehavior.STATE_EXPANDED
        }

        // Toolbar touch = scroll to top
        toolbar.setOnClickListener { recyclerView.scrollToPosition(0) }

        mainViewModel.screenState.observe(this,
                Observer { it?.let { showData(it.state, it.itemList) } })
        mainViewModel.filterEnabled.observe(this,
                Observer { it?.let { showFilterEnabled(it) } })
        updateDialogViewModel.showDialogEvent.observe(this,
                Observer { it?.let { showUpdateAvailableDialog(it.tagName, it.name, it.url) } })
    }

    override fun onDestroy() {
        super.onDestroy()
        customTabActivityHelper.setConnectionCallback(null)
    }

    override fun onStart() {
        super.onStart()
        customTabActivityHelper.bindCustomTabsService(this)

        firebaseAnalytics.setUserPropertyChromeTabs(prefs.chromeCustomTabs)
        firebaseAnalytics.setUserPropertyGridView(prefs.gridView)
        firebaseAnalytics.setUserPropertyPushEnabled(prefs.push)
        firebaseAnalytics.setUserPropertyCurrencyValue(prefs.exchangeCurrency)

        notificationController.reset()
    }

    override fun onStop() {
        super.onStop()
        customTabActivityHelper.unbindCustomTabsService(this)
    }

    override fun onResume() {
        super.onResume()
        isPaused = false
    }

    override fun onPause() {
        super.onPause()
        if (webViewPreviewDialog != null)
            webViewPreviewDialog!!.dismiss()
        isPaused = true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> {
                startActivityForResult(Intent(this, SettingsActivity::class.java), RC_SETTINGS)
                return true
            }
            R.id.action_highlight -> {
                startActivityForResult(Intent(this, HighlightActivity::class.java), RC_HIGHLIGHT)
                return true
            }
            R.id.action_feedback -> {
                firebaseAnalytics.logFeedback()
                val emailIntent = Intent(Intent.ACTION_SENDTO)
                emailIntent.data = Uri.parse("mailto:" + getString(R.string.developer_email))
                emailIntent.putExtra(Intent.EXTRA_EMAIL, getString(R.string.developer_email))
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.feedback))
                emailIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.feedback_text))
                if (emailIntent.resolveActivity(packageManager) != null) {
                    startActivity(Intent.createChooser(emailIntent, getString(R.string.send_feedback)))
                } else
                    Toast.makeText(this, R.string.error_no_email_app, Toast.LENGTH_LONG).show()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RC_HIGHLIGHT -> itemAdapter.setHighlights(prefs.favoriteList)
            RC_SETTINGS -> {
                setAdapter()
                mainViewModel.load()
            }
        }
    }

    override fun onBackPressed() {
        val params = filterView.layoutParams as CoordinatorLayout.LayoutParams
        val behavior = params.behavior as BottomSheetBehavior<*>
        if (behavior.state == BottomSheetBehavior.STATE_EXPANDED) {
            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
            return
        }
        if (backButtonWasPressed) {
            backButtonWasPressed = false
            super.onBackPressed()
        } else {
            backButtonWasPressed = true
            window?.decorView?.postDelayed({ backButtonWasPressed = false }, 2000)
            Toast.makeText(this, R.string.back_button_alert, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setAdapter() {
        var gridView = false
        var columns = -1
        val layoutManager: RecyclerView.LayoutManager
        if (prefs.gridView) {
            val metrics = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(metrics)
            columns = Math.max(1,
                    metrics.widthPixels / resources.getDimensionPixelSize(R.dimen.grid_view_item_min_width))
            layoutManager = GridLayoutManager(this, columns)
            gridView = true
        } else
            layoutManager = LinearLayoutManager(this)

        val highlights = prefs.favoriteList
        firebaseAnalytics.setUserPropertyHighlightItemsSize(highlights.size)

        itemAdapter = ItemAdapter(this, if (gridView) R.layout.item_grid_item else R.layout.item_item)
        itemAdapter.itemClick = { onItemClick(it) }
        itemAdapter.setHighlights(highlights)

        if (gridView)
            (layoutManager as GridLayoutManager).spanSizeLookup = itemAdapter.getSpanSizeLookup(columns)

        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = itemAdapter
    }

    private fun showData(@ScreenState.MainViewState state: Int, itemList: Items?) {
        swipeRefreshLayout.isRefreshing = state == ScreenState.STATE_LOADING
        when (state) {
            ScreenState.STATE_DONE -> itemAdapter.setItems(itemList)
            ScreenState.STATE_ERROR_INTERNAL -> Toast.makeText(this, R.string.error_internal, Toast.LENGTH_SHORT).show()
            ScreenState.STATE_ERROR_NETWORK -> Toast.makeText(this, R.string.error_network, Toast.LENGTH_SHORT).show()
        }
    }

    private fun showFilterEnabled(filterEnabled: Boolean) {
        filterHintView.visibility = if (filterEnabled) View.VISIBLE else View.GONE
    }

    override fun onCustomTabsConnected() {
        customTabAvailable = true
    }

    override fun onCustomTabsDisconnected() {
        customTabAvailable = false
    }

    private fun onItemClick(item: Item) {
        if (isPaused) return

        if (URLUtil.isNetworkUrl(item.url)) {
            val uri = Uri.parse(item.url)
            firebaseAnalytics.logItemView(item)

            if (prefs.chromeCustomTabs && customTabAvailable && customTabActivityHelper.mayLaunchUrl(uri, null, null)) {
                val builder = CustomTabsIntent.Builder()
                builder.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary))
                val intent = builder.build()
                intent.intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                CustomTabsHelper.addKeepAliveExtra(this, intent.intent)
                CustomTabActivityHelper.openCustomTab(this, intent, uri) { activity, u -> showUrlAsDialog(u.toString()) }
            } else
                showUrlAsDialog(item.url!!)
        } else
            Toast.makeText(this, R.string.error_invalid_uri, Toast.LENGTH_SHORT).show()
    }

    private fun showUrlAsDialog(url: String) {
        if (webViewPreviewDialog != null) webViewPreviewDialog!!.dismiss()
        webViewPreviewDialog = WebViewPreviewDialog.newInstance(url)
        webViewPreviewDialog!!.show(supportFragmentManager, null)
    }

    private fun showUpdateAvailableDialog(tagName: String, name: String, url: String) {
        UpdateDialogFragment.newInstance(tagName, name, url).show(supportFragmentManager, "update")
    }
}
