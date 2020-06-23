package ru.cherryperry.amiami.presentation.update

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.cherryperry.amiami.presentation.base.ViewModelKey

@Module
abstract class UpdateDialogModule {

    @Binds
    @IntoMap
    @ViewModelKey(UpdateDialogViewModel::class)
    abstract fun bindViewModel(viewModel: UpdateDialogViewModel): ViewModel
}
