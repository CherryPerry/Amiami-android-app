package ru.cherryperry.amiami.screen.update

import com.arellomobile.mvp.InjectViewState
import ru.cherryperry.amiami.AmiamiApplication
import ru.cherryperry.amiami.domain.update.CheckUpdateUseCase
import ru.cherryperry.amiami.domain.update.CheckUpdateUseCaseParams
import ru.cherryperry.amiami.screen.base.BasePresenter
import rx.android.schedulers.AndroidSchedulers
import javax.inject.Inject

@InjectViewState
class UpdatePresenter : BasePresenter<UpdateView>() {

    @Inject
    lateinit var checkUpdateUseCase: CheckUpdateUseCase

    init {
        AmiamiApplication.updateScreenComponent.inject(this)
        load()
    }

    private fun load() {
        addSubscription(
                checkUpdateUseCase.run(CheckUpdateUseCaseParams())
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            val result = it.updateInfo
                            if (result != null) {
                                viewState.showUpdateAvailableDialog(result.tagName, result.name, result.url)
                            }
                        }, {
                            it.printStackTrace()
                        })
        )
    }
}