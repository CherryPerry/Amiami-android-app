package ru.cherryperry.amiami.presentation.filter

import android.arch.lifecycle.ViewModel
import android.support.v4.app.Fragment
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.android.support.FragmentKey
import dagger.multibindings.IntoMap
import ru.cherryperry.amiami.presentation.base.ViewModelKey

@Module(subcomponents = [FilterFragmentSubcomponent::class])
abstract class FilterFragmentModule {

    @Binds
    @IntoMap
    @FragmentKey(FilterFragment::class)
    internal abstract fun bindFactory(
        builder: FilterFragmentSubcomponent.Builder
    ): AndroidInjector.Factory<out Fragment>

    @Binds
    @IntoMap
    @ViewModelKey(FilterViewModel::class)
    abstract fun bindViewModel(viewModel: FilterViewModel): ViewModel
}
