package ru.cherryperry.amiami.screen.activity

import android.support.v7.app.AppCompatActivity
import dagger.Module
import dagger.Provides
import ru.cherryperry.amiami.screen.base.ActivityScope

@Module
class NavigatorModule {

    @Provides
    @ActivityScope
    fun navigator(activity: AppCompatActivity) = Navigator(activity.supportFragmentManager, android.R.id.content)
}