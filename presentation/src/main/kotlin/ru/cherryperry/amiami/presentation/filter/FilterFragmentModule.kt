package ru.cherryperry.amiami.presentation.filter

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap
import ru.cherryperry.amiami.presentation.base.ViewModelKey

@Module(subcomponents = [FilterFragmentSubcomponent::class])
abstract class FilterFragmentModule {

    @Binds
    @IntoMap
    @ClassKey(FilterFragment::class)
    internal abstract fun bindFactory(builder: FilterFragmentSubcomponent.Builder): AndroidInjector.Factory<*>

    @Binds
    @IntoMap
    @ViewModelKey(FilterViewModel::class)
    abstract fun bindViewModel(viewModel: FilterViewModel): ViewModel
}
