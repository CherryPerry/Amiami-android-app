package ru.cherryperry.amiami.screen.main

import dagger.Subcomponent
import dagger.android.AndroidInjector

@Subcomponent
interface MainFragmentSubcomponent : AndroidInjector<MainFragment> {

    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<MainFragment>()
}