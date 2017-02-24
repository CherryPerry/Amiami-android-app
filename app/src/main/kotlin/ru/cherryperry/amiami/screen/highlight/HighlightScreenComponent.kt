package ru.cherryperry.amiami.screen.highlight

import dagger.Component
import ru.cherryperry.amiami.ApplicationModule
import javax.inject.Singleton

@Component(modules = arrayOf(
        ApplicationModule::class
))
@Singleton
interface HighlightScreenComponent {
    fun inject(highlightPresenter: HighlightPresenter)
}