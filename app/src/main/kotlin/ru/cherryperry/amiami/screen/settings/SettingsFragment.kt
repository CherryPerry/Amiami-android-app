package ru.cherryperry.amiami.screen.settings

import android.content.SharedPreferences
import android.os.Bundle
import android.support.v14.preference.PreferenceFragment
import android.support.v7.preference.ListPreference
import com.google.firebase.messaging.FirebaseMessaging
import dagger.android.AndroidInjection
import ru.cherryperry.amiami.AppPrefs
import ru.cherryperry.amiami.R
import ru.cherryperry.amiami.model.CurrencyRepository
import ru.cherryperry.amiami.push.MessagingService
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import javax.inject.Inject

class SettingsFragment : PreferenceFragment(), SharedPreferences.OnSharedPreferenceChangeListener {

    @Inject
    lateinit var appPrefs: AppPrefs
    @Inject
    lateinit var currencyRepository: CurrencyRepository

    private var subscription: Subscription? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreatePreferences(bundle: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.settings)
        val listPreference = findPreference(getString(R.string.key_exchange_currency)) as ListPreference
        listPreference.isEnabled = false
        listPreference.entries = arrayOf()
        listPreference.entryValues = arrayOf()

        subscription = currencyRepository.exchangeRate(false)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            if (it != null) {
                                listPreference.isEnabled = true
                                listPreference.entries = it.rates.keys.toTypedArray()
                                listPreference.entryValues = it.rates.keys.toTypedArray()
                            }
                        },
                        {
                            it.printStackTrace()
                        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        subscription?.unsubscribe()
    }

    override fun onResume() {
        super.onResume()
        appPrefs.preferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        appPrefs.preferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        if (key == getString(R.string.key_push)) {
            if (appPrefs.push) {
                FirebaseMessaging.getInstance().subscribeToTopic(MessagingService.updateTopic)
            } else {
                FirebaseMessaging.getInstance().unsubscribeFromTopic(MessagingService.updateTopic)
            }
        }
    }
}
