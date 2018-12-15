package ru.cherryperry.amiami.presentation.activity

import android.support.v7.app.AppCompatActivity
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap

@Module(subcomponents = [SingleActivitySubcomponent::class])
abstract class SingleActivityModule {

    @Binds
    @IntoMap
    @ClassKey(SingleActivity::class)
    abstract fun bindFactory(builder: SingleActivitySubcomponent.Builder): AndroidInjector.Factory<*>

    @Binds
    abstract fun appCompatActivity(singleActivity: SingleActivity): AppCompatActivity
}
