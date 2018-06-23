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
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.messaging.FirebaseMessaging
import dagger.android.support.DaggerFragment
import org.chromium.customtabsclient.shared.CustomTabsHelper
import org.chromium.customtabsdemos.CustomTabActivityHelper
import ru.cherryperry.amiami.AppPrefs
import ru.cherryperry.amiami.R
import ru.cherryperry.amiami.model.Item
import ru.cherryperry.amiami.model.Items
import ru.cherryperry.amiami.push.MessagingService
import ru.cherryperry.amiami.push.NotificationController
import ru.cherryperry.amiami.screen.activity.Navigator
import ru.cherryperry.amiami.screen.activity.OnBackKeyPressedListener
import ru.cherryperry.amiami.screen.update.UpdateDialogViewModel
import ru.cherryperry.amiami.util.FirebaseAnalyticsHelper
import ru.cherryperry.amiami.util.ViewDelegate
import ru.cherryperry.amiami.util.ViewDelegateReset
import javax.inject.Inject

class MainFragment : DaggerFragment(), CustomTabActivityHelper.ConnectionCallback, OnBackKeyPressedListener {

    companion object {
        private const val RC_SETTINGS = 704
        private const val RC_HIGHLIGHT = 507
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var navigator: Navigator

    private lateinit var mainViewModel: MainViewModel
    private lateinit var updateDialogViewModel: UpdateDialogViewModel

    // Views
    private val viewDelegateReset = ViewDelegateReset()
    private val recyclerView by ViewDelegate<RecyclerView>(R.id.recyclerView, viewDelegateReset)
    private val toolbar by ViewDelegate<Toolbar>(R.id.toolbar, viewDelegateReset)
    private val swipeRefreshLayout by ViewDelegate<SwipeRefreshLayout>(R.id.swipeRefreshLayout, viewDelegateReset)
    private val filterHintView by ViewDelegate<TextView>(R.id.filter, viewDelegateReset)
    private val filterView by ViewDelegate<FilterView>(R.id.filterView, viewDelegateReset)

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(MainViewModel::class.java)
        updateDialogViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(UpdateDialogViewModel::class.java)

        FirebaseMessaging.getInstance().unsubscribeFromTopic("updates")
        if (prefs.push)
            FirebaseMessaging.getInstance().subscribeToTopic(MessagingService.updateTopic)
        else
            FirebaseMessaging.getInstance().unsubscribeFromTopic(MessagingService.updateTopic)

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

        // Toolbar
        toolbar.setOnClickListener { recyclerView.scrollToPosition(0) }
        toolbar.inflateMenu(R.menu.main_menu)
        toolbar.setOnMenuItemClickListener { onOptionsItemSelected2(it) }

        mainViewModel.screenState.observe(this,
                Observer { it?.let { showData(it.state, it.itemList) } })
        mainViewModel.filterEnabled.observe(this,
                Observer { it?.let { showFilterEnabled(it) } })
        updateDialogViewModel.showDialogEvent.observe(this,
                Observer { it?.let { navigator.openUpdateDialog(it) } })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewDelegateReset.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
        customTabActivityHelper.setConnectionCallback(null)
    }

    override fun onStart() {
        super.onStart()
        customTabActivityHelper.bindCustomTabsService(activity!!)

        firebaseAnalytics.setUserPropertyChromeTabs(prefs.chromeCustomTabs)
        firebaseAnalytics.setUserPropertyGridView(prefs.gridView)
        firebaseAnalytics.setUserPropertyPushEnabled(prefs.push)
        firebaseAnalytics.setUserPropertyCurrencyValue(prefs.exchangeCurrency)

        notificationController.reset()
    }

    override fun onStop() {
        super.onStop()
        customTabActivityHelper.unbindCustomTabsService(activity!!)
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

    private fun onOptionsItemSelected2(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> {
                navigator.openSettings()
                return true
            }
            R.id.action_highlight -> {
                navigator.openHighlight()
                return true
            }
            R.id.action_feedback -> {
                firebaseAnalytics.logFeedback()
                val emailIntent = Intent(Intent.ACTION_SENDTO)
                emailIntent.data = Uri.parse("mailto:" + getString(R.string.developer_email))
                emailIntent.putExtra(Intent.EXTRA_EMAIL, getString(R.string.developer_email))
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.feedback))
                emailIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.feedback_text))
                if (emailIntent.resolveActivity(activity!!.packageManager) != null) {
                    startActivity(Intent.createChooser(emailIntent, getString(R.string.send_feedback)))
                } else
                    Toast.makeText(activity!!, R.string.error_no_email_app, Toast.LENGTH_LONG).show()
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

    override fun onBackPressed(): Boolean {
        val params = filterView.layoutParams as CoordinatorLayout.LayoutParams
        val behavior = params.behavior as BottomSheetBehavior<*>
        if (behavior.state == BottomSheetBehavior.STATE_EXPANDED) {
            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
            return true
        }
        return false
    }

    private fun setAdapter() {
        var gridView = false
        var columns = -1
        val layoutManager: RecyclerView.LayoutManager
        if (prefs.gridView) {
            val metrics = DisplayMetrics()
            activity!!.windowManager.defaultDisplay.getMetrics(metrics)
            columns = Math.max(1,
                    metrics.widthPixels / resources.getDimensionPixelSize(R.dimen.grid_view_item_min_width))
            layoutManager = GridLayoutManager(activity!!, columns)
            gridView = true
        } else
            layoutManager = LinearLayoutManager(activity!!)

        val highlights = prefs.favoriteList
        firebaseAnalytics.setUserPropertyHighlightItemsSize(highlights.size)

        itemAdapter = ItemAdapter(activity!!, if (gridView) R.layout.item_grid_item else R.layout.item_item)
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
            ScreenState.STATE_ERROR_INTERNAL -> Toast.makeText(activity!!, R.string.error_internal, Toast.LENGTH_SHORT).show()
            ScreenState.STATE_ERROR_NETWORK -> Toast.makeText(activity!!, R.string.error_network, Toast.LENGTH_SHORT).show()
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
                builder.setToolbarColor(ContextCompat.getColor(activity!!, R.color.colorPrimary))
                val intent = builder.build()
                intent.intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                CustomTabsHelper.addKeepAliveExtra(activity!!, intent.intent)
                CustomTabActivityHelper.openCustomTab(activity!!, intent, uri) { activity, u -> showUrlAsDialog(u.toString()) }
            } else
                showUrlAsDialog(item.url!!)
        } else
            Toast.makeText(activity!!, R.string.error_invalid_uri, Toast.LENGTH_SHORT).show()
    }

    private fun showUrlAsDialog(url: String) {
        if (webViewPreviewDialog != null) webViewPreviewDialog!!.dismiss()
        webViewPreviewDialog = WebViewPreviewDialog.newInstance(url)
        webViewPreviewDialog!!.show(childFragmentManager, null)
    }
}
