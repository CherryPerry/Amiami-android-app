package ru.cherryperry.amiami.screen.highlight

import dagger.Subcomponent
import dagger.android.AndroidInjector

@Subcomponent
interface HighlightActivitySubcomponent : AndroidInjector<HighlightActivity> {

    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<HighlightActivity>()
}