package ru.cherryperry.amiami.screen.settings

import android.arch.lifecycle.ViewModel
import android.support.v4.app.Fragment
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.android.support.FragmentKey
import dagger.multibindings.IntoMap
import ru.cherryperry.amiami.screen.base.ViewModelKey

@Module(subcomponents = [SettingsFragmentSubcomponent::class])
abstract class SettingsFragmentModule {

    @Binds
    @IntoMap
    @FragmentKey(SettingsFragment::class)
    internal abstract fun bindFactory(builder: SettingsFragmentSubcomponent.Builder): AndroidInjector.Factory<out Fragment>

    @Binds
    @IntoMap
    @ViewModelKey(SettingsViewModel::class)
    abstract fun bindViewModel(viewModel: SettingsViewModel): ViewModel
}