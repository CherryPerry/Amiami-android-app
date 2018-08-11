package ru.cherryperry.amiami.presentation.base

import android.arch.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module

@Module
abstract class AppViewModelFactoryModule {

    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: AppViewModelFactory): ViewModelProvider.Factory
}
