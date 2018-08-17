package ru.cherryperry.amiami.presentation.settings

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.view.ViewCompat
import android.support.v7.preference.ListPreference
import android.support.v7.preference.PreferenceFragmentCompat
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.android.support.AndroidSupportInjection
import ru.cherryperry.amiami.R
import ru.cherryperry.amiami.presentation.activity.Navigator
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
        val listPreference = findPreference(getString(R.string.key_exchange_currency)) as ListPreference
        viewModel.currencySetting.observe(this, Observer {
            it?.apply {
                listPreference.isEnabled = enabled
                listPreference.entries = entries
                listPreference.entryValues = values
            }
        })
        lifecycle.addObserver(viewModel)
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
    }
}
