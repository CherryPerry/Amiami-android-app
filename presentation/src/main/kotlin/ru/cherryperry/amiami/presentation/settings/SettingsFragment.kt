package ru.cherryperry.amiami.presentation.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import dagger.android.support.AndroidSupportInjection
import ru.cherryperry.amiami.R
import ru.cherryperry.amiami.presentation.activity.Navigator
import ru.cherryperry.amiami.presentation.base.NotNullObserver
import ru.cherryperry.amiami.presentation.util.addPaddingBottomToFitBottomSystemInset
import javax.inject.Inject

class SettingsFragment : PreferenceFragmentCompat() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var navigator: Navigator

    private lateinit var viewModel: SettingsViewModel

    override fun onCreatePreferences(bundle: Bundle?, rootKey: String?) {
        AndroidSupportInjection.inject(this)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(SettingsViewModel::class.java)
        addPreferencesFromResource(R.xml.settings)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val parentView = view as ViewGroup
        val toolbarLayout = LayoutInflater.from(parentView.context).inflate(
            R.layout.settings_toolbar, parentView, false)
        val toolbar = toolbarLayout.findViewById<Toolbar>(R.id.toolbar)
        navigator.configureToolbar(toolbar)
        parentView.addView(toolbarLayout, 0)
        ViewCompat.requestApplyInsets(toolbar)

        val currencyListPreference = findPreference(getString(R.string.key_exchange_currency)) as ListPreference
        viewModel.currencySetting.observe(viewLifecycleOwner, NotNullObserver {
            currencyListPreference.isEnabled = it.enabled
            currencyListPreference.entries = it.entries
            currencyListPreference.entryValues = it.values
        })
        val pushListPreference = findPreference(getString(R.string.key_push_counter_filter_string)) as ListPreference
        viewModel.notificationsEnabled.observe(viewLifecycleOwner, NotNullObserver {
            pushListPreference.isEnabled = it
        })

        val recyclerView = (parentView.getChildAt(1) as ViewGroup).getChildAt(0)
        addPaddingBottomToFitBottomSystemInset(recyclerView)
        ViewCompat.requestApplyInsets(recyclerView)
    }
}
