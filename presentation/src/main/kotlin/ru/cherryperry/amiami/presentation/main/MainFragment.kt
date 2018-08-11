package ru.cherryperry.amiami.presentation.main

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.CoordinatorLayout
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isGone
import dagger.android.support.DaggerFragment
import ru.cherryperry.amiami.R
import ru.cherryperry.amiami.data.prefs.AppPrefs
import ru.cherryperry.amiami.domain.model.Item
import ru.cherryperry.amiami.domain.model.Model
import ru.cherryperry.amiami.presentation.activity.Navigator
import ru.cherryperry.amiami.presentation.activity.OnBackKeyPressedListener
import ru.cherryperry.amiami.presentation.base.NotNullObserver
import ru.cherryperry.amiami.presentation.main.adapter.ItemAdapter
import ru.cherryperry.amiami.presentation.main.adapter.ItemSpanSizeLookup
import ru.cherryperry.amiami.presentation.push.NotificationController
import ru.cherryperry.amiami.presentation.update.UpdateDialogViewModel
import ru.cherryperry.amiami.presentation.util.FirebaseAnalyticsHelper
import ru.cherryperry.amiami.presentation.util.ViewDelegate
import ru.cherryperry.amiami.presentation.util.ViewDelegateReset
import javax.inject.Inject

class MainFragment : DaggerFragment(), OnBackKeyPressedListener {

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
    private val filterView by ViewDelegate<View>(R.id.filterView, viewDelegateReset)
    private val errorView by ViewDelegate<View>(R.id.errorLayout, viewDelegateReset)
    private val errorText by ViewDelegate<TextView>(R.id.errorText, viewDelegateReset)
    private val itemAdapter = ItemAdapter()

    @Inject
    lateinit var prefs: AppPrefs
    @Inject
    lateinit var firebaseAnalytics: FirebaseAnalyticsHelper
    @Inject
    lateinit var notificationController: NotificationController

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(MainViewModel::class.java)
        updateDialogViewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(UpdateDialogViewModel::class.java)

        setAdapter()

        swipeRefreshLayout.setOnRefreshListener { mainViewModel.load() }

        // Toolbar
        toolbar.setOnClickListener { recyclerView.scrollToPosition(0) }
        toolbar.inflateMenu(R.menu.main_menu)
        toolbar.setOnMenuItemClickListener { onToolbarMenuItemClick(it) }

        mainViewModel.screenState.observe(this,
            NotNullObserver { showData(it.state, it.list) })
        mainViewModel.filterEnabled.observe(this,
            NotNullObserver { showFilterEnabled(it) })
        updateDialogViewModel.showDialogEvent.observe(this,
            NotNullObserver { navigator.openUpdateDialog(it) })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewDelegateReset.onDestroyView()
    }

    override fun onStart() {
        super.onStart()

        firebaseAnalytics.setUserPropertyChromeTabs(prefs.chromeCustomTabs.value)
        firebaseAnalytics.setUserPropertyGridView(prefs.gridView.value)
        firebaseAnalytics.setUserPropertyPushEnabled(prefs.push.value)
        firebaseAnalytics.setUserPropertyCurrencyValue(prefs.exchangeCurrency.value)

        notificationController.reset()
    }

    private fun onToolbarMenuItemClick(item: MenuItem): Boolean =
        when (item.itemId) {
            R.id.action_settings -> {
                navigator.openSettings()
                true
            }
            R.id.action_highlight -> {
                navigator.openHighlight()
                true
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
                true
            }
            else -> false
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
        itemAdapter.onItemClick = this::onItemClick
        recyclerView.adapter = itemAdapter
        recyclerView.addItemDecoration(PaddingItemDecorator(recyclerView.context))
        recyclerView.post {
            val layoutManager = recyclerView.layoutManager as GridLayoutManager
            val columns = Math.max(1, recyclerView.width /
                resources.getDimensionPixelSize(R.dimen.grid_view_item_min_width))
            layoutManager.spanCount = columns
            layoutManager.spanSizeLookup = ItemSpanSizeLookup(itemAdapter, layoutManager)
        }
    }

    private fun showData(@ScreenState.MainViewState state: Int, list: List<Model>) {
        swipeRefreshLayout.isRefreshing = state == ScreenState.STATE_LOADING
        when (state) {
            ScreenState.STATE_DONE -> {
                recyclerView.isGone = false
                errorView.isGone = true
                itemAdapter.submitList(list)
            }
            ScreenState.STATE_ERROR_INTERNAL -> {
                recyclerView.isGone = true
                errorView.isGone = false
                errorText.setText(R.string.error_internal)
            }
            ScreenState.STATE_ERROR_NETWORK -> {
                recyclerView.isGone = true
                errorView.isGone = false
                errorText.setText(R.string.error_network)
            }
        }
    }

    private fun showFilterEnabled(filterEnabled: Boolean) {
        filterHintView.visibility = if (filterEnabled) View.VISIBLE else View.GONE
    }

    private fun onItemClick(item: Item) {
        firebaseAnalytics.logItemView(item)
        navigator.openItem(item)
    }
}
