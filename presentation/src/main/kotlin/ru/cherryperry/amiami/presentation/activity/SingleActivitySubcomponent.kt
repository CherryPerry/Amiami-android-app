package ru.cherryperry.amiami.presentation.activity

import dagger.Subcomponent
import dagger.android.AndroidInjector
import ru.cherryperry.amiami.presentation.base.ActivityScope
import ru.cherryperry.amiami.presentation.filter.FilterFragmentModule
import ru.cherryperry.amiami.presentation.highlight.HighlightFragmentModule
import ru.cherryperry.amiami.presentation.main.MainFragmentModule
import ru.cherryperry.amiami.presentation.settings.SettingsFragmentModule
import ru.cherryperry.amiami.presentation.update.UpdateDialogModule

@ActivityScope
@Subcomponent(modules = [
    NavigatorModule::class,
    MainFragmentModule::class,
    HighlightFragmentModule::class,
    SettingsFragmentModule::class,
    UpdateDialogModule::class,
    FilterFragmentModule::class
])
interface SingleActivitySubcomponent : AndroidInjector<SingleActivity> {

    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<SingleActivity>()
}
