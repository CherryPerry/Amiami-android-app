package ru.cherryperry.amiami.screen.activity

import dagger.Subcomponent
import dagger.android.AndroidInjector
import ru.cherryperry.amiami.push.MessagingServiceModule
import ru.cherryperry.amiami.screen.base.ActivityScope
import ru.cherryperry.amiami.screen.highlight.HighlightFragmentModule
import ru.cherryperry.amiami.screen.main.MainFragmentModule
import ru.cherryperry.amiami.screen.settings.SettingsFragmentModule
import ru.cherryperry.amiami.screen.update.UpdateDialogModule

@ActivityScope
@Subcomponent(modules = [
    NavigatorModule::class,
    MainFragmentModule::class,
    HighlightFragmentModule::class,
    MessagingServiceModule::class,
    SettingsFragmentModule::class,
    UpdateDialogModule::class
])
interface SingleActivitySubcomponent : AndroidInjector<SingleActivity> {

    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<SingleActivity>()
}