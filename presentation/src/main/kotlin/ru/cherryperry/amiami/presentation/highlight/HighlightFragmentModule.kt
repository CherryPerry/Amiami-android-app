package ru.cherryperry.amiami.presentation.highlight

import android.arch.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap
import ru.cherryperry.amiami.presentation.base.ViewModelKey

@Module(subcomponents = [HighlightFragmentSubcomponent::class])
abstract class HighlightFragmentModule {

    @Binds
    @IntoMap
    @ClassKey(HighlightFragment::class)
    internal abstract fun bindFactory(builder: HighlightFragmentSubcomponent.Builder): AndroidInjector.Factory<*>

    @Binds
    @IntoMap
    @ViewModelKey(HighlightViewModel::class)
    abstract fun bindViewModel(viewModel: HighlightViewModel): ViewModel
}
