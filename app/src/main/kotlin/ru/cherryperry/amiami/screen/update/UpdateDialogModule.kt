package ru.cherryperry.amiami.screen.update

import android.arch.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.cherryperry.amiami.screen.base.ViewModelKey

@Module
abstract class UpdateDialogModule {

    @Binds
    @IntoMap
    @ViewModelKey(UpdateDialogViewModel::class)
    abstract fun bindViewModel(viewModel: UpdateDialogViewModel): ViewModel
}