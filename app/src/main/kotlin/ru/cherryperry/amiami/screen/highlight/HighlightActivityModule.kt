package ru.cherryperry.amiami.screen.highlight

import android.app.Activity
import android.arch.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ActivityKey
import dagger.android.AndroidInjector
import dagger.multibindings.IntoMap
import ru.cherryperry.amiami.screen.base.ViewModelKey

@Module(subcomponents = [HighlightActivitySubcomponent::class])
abstract class HighlightActivityModule {

    @Binds
    @IntoMap
    @ActivityKey(HighlightActivity::class)
    internal abstract fun bindFactory(builder: HighlightActivitySubcomponent.Builder): AndroidInjector.Factory<out Activity>

    @Binds
    @IntoMap
    @ViewModelKey(HighlightViewModel::class)
    abstract fun bindViewModel(viewModel: HighlightViewModel): ViewModel
}