package ru.cherryperry.amiami.presentation.main

import android.arch.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap
import ru.cherryperry.amiami.presentation.base.ViewModelKey

@Module(subcomponents = [MainFragmentSubcomponent::class])
abstract class MainFragmentModule {

    @Binds
    @IntoMap
    @ClassKey(MainFragment::class)
    internal abstract fun bindFactory(builder: MainFragmentSubcomponent.Builder): AndroidInjector.Factory<*>

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindViewModel(viewModel: MainViewModel): ViewModel
}
