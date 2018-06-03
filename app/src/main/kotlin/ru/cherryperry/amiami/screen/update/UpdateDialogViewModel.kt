package ru.cherryperry.amiami.screen.update

import ru.cherryperry.amiami.domain.data.UpdateInfo
import ru.cherryperry.amiami.domain.update.CheckUpdateUseCase
import ru.cherryperry.amiami.domain.update.CheckUpdateUseCaseParams
import ru.cherryperry.amiami.screen.base.BaseViewModel
import ru.cherryperry.amiami.screen.base.SingleLiveEvent
import rx.android.schedulers.AndroidSchedulers
import javax.inject.Inject

class UpdateDialogViewModel @Inject constructor(
        checkUpdateUseCase: CheckUpdateUseCase
) : BaseViewModel() {

    val showDialogEvent = SingleLiveEvent<UpdateInfo>()

    init {
        this += checkUpdateUseCase.run(CheckUpdateUseCaseParams())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ it.updateInfo?.also { showDialogEvent.value = it } }, { it.printStackTrace() })
    }
}