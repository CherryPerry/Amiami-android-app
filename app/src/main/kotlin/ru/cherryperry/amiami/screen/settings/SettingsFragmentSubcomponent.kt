package ru.cherryperry.amiami.screen.main

import dagger.Subcomponent
import dagger.android.AndroidInjector
import ru.cherryperry.amiami.screen.settings.SettingsFragment

@Subcomponent
interface SettingsFragmentSubcomponent : AndroidInjector<SettingsFragment> {

    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<SettingsFragment>()
}