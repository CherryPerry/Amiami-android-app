package ru.cherryperry.amiami.presentation.settings

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap
import ru.cherryperry.amiami.presentation.base.ViewModelKey

@Module(subcomponents = [SettingsFragmentSubcomponent::class])
abstract class SettingsFragmentModule {

    @Binds
    @IntoMap
    @ClassKey(SettingsFragment::class)
    internal abstract fun bindFactory(builder: SettingsFragmentSubcomponent.Builder): AndroidInjector.Factory<*>

    @Binds
    @IntoMap
    @ViewModelKey(SettingsViewModel::class)
    abstract fun bindViewModel(viewModel: SettingsViewModel): ViewModel
}
