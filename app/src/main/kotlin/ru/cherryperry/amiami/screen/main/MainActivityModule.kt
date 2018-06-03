package ru.cherryperry.amiami.screen.main

import android.app.Activity
import android.arch.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ActivityKey
import dagger.android.AndroidInjector
import dagger.multibindings.IntoMap
import ru.cherryperry.amiami.screen.base.ViewModelKey

@Module(subcomponents = [MainActivitySubcomponent::class])
abstract class MainActivityModule {

    @Binds
    @IntoMap
    @ActivityKey(MainActivity::class)
    internal abstract fun bindFactory(builder: MainActivitySubcomponent.Builder): AndroidInjector.Factory<out Activity>

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindViewModel(viewModel: MainViewModel): ViewModel
}