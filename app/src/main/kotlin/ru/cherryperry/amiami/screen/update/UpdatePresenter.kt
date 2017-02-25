package ru.cherryperry.amiami.screen.update

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import ru.cherryperry.amiami.AmiamiApplication
import ru.cherryperry.amiami.BuildConfig
import ru.cherryperry.amiami.network.ApiProvider
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import javax.inject.Inject

@InjectViewState
class UpdatePresenter : MvpPresenter<UpdateView>() {

    @Inject
    lateinit var apiProvider: ApiProvider

    private var subscription: Subscription? = null

    init {
        AmiamiApplication.updateScreenComponent.inject(this)
        load()
    }

    override fun onDestroy() {
        super.onDestroy()
        subscription?.unsubscribe()
    }

    private fun load() {
        subscription = apiProvider.gitHubApi()
                .flatMap { it.latestRelease() }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.tagName == null || it.name == null) {
                        return@subscribe
                    }
                    if (it.tagName == BuildConfig.VERSION_NAME) {
                        return@subscribe
                    }
                    val assetUrl = it.assets?.first()?.url ?: return@subscribe
                    viewState.showUpdateAvailableDialog(it.tagName, it.name, assetUrl)
                }, { it.printStackTrace() })
    }
}