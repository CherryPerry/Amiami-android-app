package ru.cherryperry.amiami.presentation.update

import ru.cherryperry.amiami.domain.model.UpdateInfo
import ru.cherryperry.amiami.domain.update.CheckUpdateUseCase
import ru.cherryperry.amiami.presentation.base.BaseViewModel
import ru.cherryperry.amiami.presentation.base.SingleLiveEvent
import rx.android.schedulers.AndroidSchedulers
import javax.inject.Inject

class UpdateDialogViewModel @Inject constructor(
    checkUpdateUseCase: CheckUpdateUseCase
) : BaseViewModel() {

    val showDialogEvent = SingleLiveEvent<UpdateInfo>()

    init {
        this += checkUpdateUseCase.run(Any())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ showDialogEvent.value = it }, { it.printStackTrace() })
    }
}
