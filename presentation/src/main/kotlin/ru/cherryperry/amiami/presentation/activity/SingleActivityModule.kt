package ru.cherryperry.amiami.presentation.activity

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import dagger.Binds
import dagger.Module
import dagger.android.ActivityKey
import dagger.android.AndroidInjector
import dagger.multibindings.IntoMap

@Module(subcomponents = [SingleActivitySubcomponent::class])
abstract class SingleActivityModule {

    @Binds
    @IntoMap
    @ActivityKey(SingleActivity::class)
    abstract fun bindFactory(builder: SingleActivitySubcomponent.Builder): AndroidInjector.Factory<out Activity>

    @Binds
    abstract fun appCompatActivity(singleActivity: SingleActivity): AppCompatActivity
}
