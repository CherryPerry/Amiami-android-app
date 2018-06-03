package ru.cherryperry.amiami.screen.main

import android.app.Fragment
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.android.FragmentKey
import dagger.multibindings.IntoMap
import ru.cherryperry.amiami.screen.settings.SettingsFragment

@Module(subcomponents = [SettingsFragmentSubcomponent::class])
abstract class SettingsFragmentModule {

    @Binds
    @IntoMap
    @FragmentKey(SettingsFragment::class)
    internal abstract fun bindFactory(builder: SettingsFragmentSubcomponent.Builder): AndroidInjector.Factory<out Fragment>
}