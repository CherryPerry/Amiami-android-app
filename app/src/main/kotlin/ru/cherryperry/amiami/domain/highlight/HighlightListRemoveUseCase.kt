package ru.cherryperry.amiami.domain.highlight

import ru.cherryperry.amiami.AppPrefs
import ru.cherryperry.amiami.domain.CompletableUseCase
import ru.cherryperry.amiami.domain.UseCaseParam
import rx.Completable
import java.util.*
import javax.inject.Inject

/**
 * Remove item from hightlight list
 */
class HighlightListRemoveUseCase @Inject constructor(private val appPrefs: AppPrefs) :
        CompletableUseCase<HighlightListRemoveUseCaseParams>() {

    override fun run(param: HighlightListRemoveUseCaseParams): Completable {
        return Completable.fromAction {
            val set = TreeSet(appPrefs.favoriteList)
            set.remove(param.item)
            appPrefs.favoriteList = set
        }
    }
}

class HighlightListRemoveUseCaseParams(val item: String) : UseCaseParam()