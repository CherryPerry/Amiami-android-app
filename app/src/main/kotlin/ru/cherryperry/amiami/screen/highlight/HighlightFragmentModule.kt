package ru.cherryperry.amiami.screen.highlight

import android.arch.lifecycle.ViewModel
import android.support.v4.app.Fragment
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.android.support.FragmentKey
import dagger.multibindings.IntoMap
import ru.cherryperry.amiami.screen.base.ViewModelKey

@Module(subcomponents = [HighlightFragmentSubcomponent::class])
abstract class HighlightFragmentModule {

    @Binds
    @IntoMap
    @FragmentKey(HighlightFragment::class)
    internal abstract fun bindFactory(builder: HighlightFragmentSubcomponent.Builder): AndroidInjector.Factory<out Fragment>

    @Binds
    @IntoMap
    @ViewModelKey(HighlightViewModel::class)
    abstract fun bindViewModel(viewModel: HighlightViewModel): ViewModel
}