package ru.cherryperry.amiami.presentation.update

import io.reactivex.android.schedulers.AndroidSchedulers
import ru.cherryperry.amiami.domain.model.UpdateInfo
import ru.cherryperry.amiami.domain.update.CheckUpdateUseCase
import ru.cherryperry.amiami.presentation.base.BaseViewModel
import ru.cherryperry.amiami.presentation.base.SingleLiveEvent
import javax.inject.Inject

class UpdateDialogViewModel @Inject constructor(
    checkUpdateUseCase: CheckUpdateUseCase
) : BaseViewModel() {

    val showDialogEvent = SingleLiveEvent<UpdateInfo>()

    init {
        this += checkUpdateUseCase.run(Unit)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ showDialogEvent.value = it }, { it.printStackTrace() })
    }
}
