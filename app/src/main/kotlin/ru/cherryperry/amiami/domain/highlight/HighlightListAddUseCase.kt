package ru.cherryperry.amiami.domain.highlight

import ru.cherryperry.amiami.AppPrefs
import ru.cherryperry.amiami.domain.CompletableUseCase
import ru.cherryperry.amiami.domain.UseCaseParam
import rx.Completable
import java.util.*
import javax.inject.Inject

/**
 * Add item to hightlight list
 */
class HighlightListAddUseCase @Inject constructor(private val appPrefs: AppPrefs) :
        CompletableUseCase<HighlightListAddUseCaseParams>() {

    override fun run(param: HighlightListAddUseCaseParams): Completable {
        return Completable.fromAction {
            val set = TreeSet(appPrefs.favoriteList)
            set.add(param.item)
            appPrefs.favoriteList = set
        }
    }
}

class HighlightListAddUseCaseParams(val item: String) : UseCaseParam()