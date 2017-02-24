package ru.cherryperry.amiami

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import ru.cherryperry.amiami.model.ExchangeRate
import ru.cherryperry.amiami.util.BooleanPreference
import ru.cherryperry.amiami.util.IntPreference
import ru.cherryperry.amiami.util.StringPreference
import ru.cherryperry.amiami.util.StringSetPreference
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppPrefs @Inject constructor(context: Context) {
    val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    var push by BooleanPreference(context, R.string.key_push, false)
    var chromeCustomTabs by BooleanPreference(context, R.string.key_chromeCustomTabs, true)
    var gridView by BooleanPreference(context, R.string.key_gridView, true)
    var pushCounter by IntPreference(context, R.string.key_push_counter, 0)
    var priceMin by IntPreference(context, R.string.key_price_min, 0)
    var priceMax by IntPreference(context, R.string.key_price_max, Int.MAX_VALUE)
    var searchTerm by StringPreference(context, R.string.key_search_filter, "")
    var favoriteList by StringSetPreference(context, R.string.key_favorites)
    var favoritesOnly by BooleanPreference(context, R.string.key_favorites_only, false)
    var lastServer by StringPreference(context, R.string.key_last_server, "")
    var lastExchanges by StringPreference(context, R.string.key_last_exchanges, "")
    var exchangeCurrency by StringPreference(context, R.string.key_exchange_currency, ExchangeRate.DEFAULT)
}