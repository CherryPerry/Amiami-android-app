package ru.cherryperry.amiami.presentation.highlight

import dagger.Subcomponent
import dagger.android.AndroidInjector

@Subcomponent
interface HighlightFragmentSubcomponent : AndroidInjector<HighlightFragment> {

    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<HighlightFragment>()
}
