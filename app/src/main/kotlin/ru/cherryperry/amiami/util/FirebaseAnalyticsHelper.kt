package ru.cherryperry.amiami.util

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import ru.cherryperry.amiami.model.Item
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseAnalyticsHelper @Inject constructor(context: Context) {
    private val firebaseAnalytics = FirebaseAnalytics.getInstance(context)

    fun logItemView(item: Item) {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, item.url)
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, item.name)
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "figure")
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
    }

    fun logSearch(minPrice: Int, maxPrice: Int, term: String?) {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.SEARCH_TERM, term ?: "")
        bundle.putInt("search_min_price", minPrice)
        bundle.putInt("search_max_price", maxPrice)
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SEARCH, bundle)
    }

    fun logFeedback() {
        firebaseAnalytics.logEvent("feedback", null)
    }

    fun setUserPropertyChromeTabs(enabled: Boolean) {
        firebaseAnalytics.setUserProperty("chrome_tabs", enabled.toString())
    }

    fun setUserPropertyGridView(enabled: Boolean) {
        firebaseAnalytics.setUserProperty("grid_view", enabled.toString())
    }

    fun setUserPropertyPushEnabled(enabled: Boolean) {
        firebaseAnalytics.setUserProperty("push_enabled", enabled.toString())
    }

    fun setUserPropertyHighlightItemsSize(size: Int) {
        firebaseAnalytics.setUserProperty("highligh_items", size.toString())
    }

    fun setUserPropertyCurrencyValue(currency: String) {
        firebaseAnalytics.setUserProperty("currency", currency)
    }
}