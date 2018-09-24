package ru.cherryperry.amiami.data.prefs

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.support.annotation.StringRes
import ru.cherryperry.amiami.data.R
import ru.cherryperry.amiami.domain.model.ExchangeRates
import javax.inject.Inject

class AppPrefs @Inject constructor(
    private val context: Context
) {

    private val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    val push = BooleanPreference(key(R.string.key_push), false, preferences)
    val pushCounter = IntPreference(key(R.string.key_push_counter), 0, preferences)
    val chromeCustomTabs = BooleanPreference(key(R.string.key_chromeCustomTabs), true, preferences)
    val gridView = BooleanPreference(key(R.string.key_gridView), true, preferences)
    val favoritesOnly = BooleanPreference(key(R.string.key_favorites_only), false, preferences)
    val exchangeCurrency = StringPreference(key(R.string.key_exchange_currency), ExchangeRates.DEFAULT, preferences)
    val showNames = BooleanPreference(key(R.string.key_show_names), true, preferences)

    private fun key(@StringRes stringRes: Int) = context.getString(stringRes)
}
