package ru.cherryperry.amiami.presentation.activity

import dagger.Module
import dagger.Provides
import ru.cherryperry.amiami.data.prefs.AppPrefs
import ru.cherryperry.amiami.presentation.base.ActivityScope

@Module
class NavigatorModule {

    @Provides
    @ActivityScope
    fun navigator(activity: SingleActivity, appPrefs: AppPrefs) =
        Navigator(activity.supportFragmentManager, android.R.id.content, activity, appPrefs)
}
