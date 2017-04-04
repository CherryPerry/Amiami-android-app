package ru.cherryperry.amiami.domain.highlight

import android.content.SharedPreferences
import ru.cherryperry.amiami.AppPrefs
import ru.cherryperry.amiami.domain.ObservalbeUseCase
import ru.cherryperry.amiami.domain.UseCaseParam
import ru.cherryperry.amiami.domain.UseCaseResult
import rx.Emitter
import rx.Observable
import java.util.concurrent.atomic.AtomicReference
import javax.inject.Inject

/**
 * Get list of highlight rules
 */
class HighlightListUseCase @Inject constructor(private val appPrefs: AppPrefs) :
        ObservalbeUseCase<HighlightListUseCaseParams, HighlightListUseCaseResult>() {

    override fun run(param: HighlightListUseCaseParams): Observable<HighlightListUseCaseResult> {
        val observerReference = AtomicReference<SharedPreferences.OnSharedPreferenceChangeListener>()
        return Observable.create<HighlightListUseCaseResult>({
            val observer = SharedPreferences.OnSharedPreferenceChangeListener { _, _ ->
                it.onNext(HighlightListUseCaseResult(getAsSortedList()))
            }
            appPrefs.preferences.registerOnSharedPreferenceChangeListener(observer)
            observerReference.set(observer)
            observer.onSharedPreferenceChanged(null, null)
        }, Emitter.BackpressureMode.LATEST)
                .doOnUnsubscribe {
                    observerReference.get()?.let {
                        appPrefs.preferences.unregisterOnSharedPreferenceChangeListener(it)
                    }
                }
    }

    private fun getAsSortedList(): ArrayList<String> {
        val list = ArrayList(appPrefs.favoriteList)
        list.sort()
        return list
    }
}

class HighlightListUseCaseParams : UseCaseParam()

class HighlightListUseCaseResult(val list: ArrayList<String>) : UseCaseResult()