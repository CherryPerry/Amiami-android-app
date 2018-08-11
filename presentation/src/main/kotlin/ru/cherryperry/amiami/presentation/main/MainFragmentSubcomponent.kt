package ru.cherryperry.amiami.presentation.main

import dagger.Subcomponent
import dagger.android.AndroidInjector

@Subcomponent
interface MainFragmentSubcomponent : AndroidInjector<MainFragment> {

    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<MainFragment>()
}
