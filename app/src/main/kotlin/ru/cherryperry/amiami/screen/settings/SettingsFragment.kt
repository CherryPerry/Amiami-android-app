package ru.cherryperry.amiami.screen.settings

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v14.preference.PreferenceFragment
import android.support.v4.app.FragmentActivity
import android.support.v7.preference.ListPreference
import dagger.android.AndroidInjection
import ru.cherryperry.amiami.R
import javax.inject.Inject


class SettingsFragment : PreferenceFragment(), LifecycleOwner {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var lifecycleRegistry: LifecycleRegistry = LifecycleRegistry(this)
    private lateinit var viewModel: SettingsViewModel

    override fun onCreatePreferences(bundle: Bundle?, rootKey: String?) {
        AndroidInjection.inject(this)
        // PreferenceFragment is not support fragment!
        viewModel = ViewModelProviders.of(activity as FragmentActivity, viewModelFactory)
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleRegistry.markState(Lifecycle.State.CREATED)
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycleRegistry.markState(Lifecycle.State.DESTROYED)
    }

    override fun onStart() {
        super.onStart()
        lifecycleRegistry.markState(Lifecycle.State.STARTED)
    }

    override fun onResume() {
        super.onResume()
        lifecycleRegistry.markState(Lifecycle.State.RESUMED)
    }

    override fun onPause() {
        super.onPause()
        lifecycleRegistry.markState(Lifecycle.State.STARTED)
    }

    override fun getLifecycle(): Lifecycle {
        return lifecycleRegistry
    }
}
