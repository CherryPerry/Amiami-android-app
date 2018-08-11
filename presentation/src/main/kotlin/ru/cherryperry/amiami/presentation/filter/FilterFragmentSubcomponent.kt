package ru.cherryperry.amiami.presentation.filter

import dagger.Subcomponent
import dagger.android.AndroidInjector

@Subcomponent
interface FilterFragmentSubcomponent : AndroidInjector<FilterFragment> {

    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<FilterFragment>()
}
