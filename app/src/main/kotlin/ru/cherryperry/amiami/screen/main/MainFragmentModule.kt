package ru.cherryperry.amiami.screen.main

import android.arch.lifecycle.ViewModel
import android.support.v4.app.Fragment
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.android.support.FragmentKey
import dagger.multibindings.IntoMap
import ru.cherryperry.amiami.screen.base.ViewModelKey

@Module(subcomponents = [MainFragmentSubcomponent::class])
abstract class MainFragmentModule {

    @Binds
    @IntoMap
    @FragmentKey(MainFragment::class)
    internal abstract fun bindFactory(builder: MainFragmentSubcomponent.Builder): AndroidInjector.Factory<out Fragment>

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindViewModel(viewModel: MainViewModel): ViewModel
}