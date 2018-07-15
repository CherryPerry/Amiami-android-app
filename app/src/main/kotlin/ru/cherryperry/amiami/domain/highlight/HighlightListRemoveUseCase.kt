package ru.cherryperry.amiami.domain.highlight

import ru.cherryperry.amiami.AppPrefs
import ru.cherryperry.amiami.domain.CompletableUseCase
import rx.Completable
import java.util.TreeSet
import javax.inject.Inject

/**
 * Remove item from hightlight list
 */
class HighlightListRemoveUseCase @Inject constructor(
    private val appPrefs: AppPrefs
) : CompletableUseCase<String>() {

    override fun run(param: String): Completable {
        return Completable.fromAction {
            val set = TreeSet(appPrefs.favoriteList)
            set.remove(param)
            appPrefs.favoriteList = set
        }
    }
}