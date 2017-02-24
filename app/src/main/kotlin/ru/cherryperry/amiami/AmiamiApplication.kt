package ru.cherryperry.amiami

import android.app.Application
import ru.cherryperry.amiami.push.DaggerMessagingServiceComponent
import ru.cherryperry.amiami.push.MessagingServiceComponent
import ru.cherryperry.amiami.screen.highlight.DaggerHighlightScreenComponent
import ru.cherryperry.amiami.screen.highlight.HighlightScreenComponent
import ru.cherryperry.amiami.screen.main.DaggerMainScreenComponent
import ru.cherryperry.amiami.screen.main.MainScreenComponent
import ru.cherryperry.amiami.screen.settings.DaggerSettingsScreenComponent
import ru.cherryperry.amiami.screen.settings.SettingsScreenComponent

class AmiamiApplication : Application() {
    companion object {
        lateinit var mainScreenComponent: MainScreenComponent
        lateinit var settingsScreenComponent: SettingsScreenComponent
        lateinit var highlightScreenComponent: HighlightScreenComponent
        lateinit var messagingServiceComponent: MessagingServiceComponent
    }

    override fun onCreate() {
        super.onCreate()

        val applicationModule = ApplicationModule(this)

        mainScreenComponent = DaggerMainScreenComponent.builder()
                .applicationModule(applicationModule)
                .build()

        settingsScreenComponent = DaggerSettingsScreenComponent.builder()
                .applicationModule(applicationModule)
                .build()

        highlightScreenComponent = DaggerHighlightScreenComponent.builder()
                .applicationModule(applicationModule)
                .build()

        messagingServiceComponent = DaggerMessagingServiceComponent.builder()
                .applicationModule(applicationModule)
                .build()
    }
}
