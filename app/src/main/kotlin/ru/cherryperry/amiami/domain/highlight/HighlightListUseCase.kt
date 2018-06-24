package ru.cherryperry.amiami.domain.highlight

import android.content.SharedPreferences
import ru.cherryperry.amiami.AppPrefs
import ru.cherryperry.amiami.domain.ObservableUseCase
import rx.Emitter
import rx.Observable
import java.util.concurrent.atomic.AtomicReference
import javax.inject.Inject

/**
 * Get list of highlight rules
 */
class HighlightListUseCase @Inject constructor(
        private val appPrefs: AppPrefs
) : ObservableUseCase<Any, ArrayList<String>>() {

    override fun run(param: Any): Observable<ArrayList<String>> {
        val observerReference = AtomicReference<SharedPreferences.OnSharedPreferenceChangeListener>()
        return Observable.create<ArrayList<String>>({
            val observer = SharedPreferences.OnSharedPreferenceChangeListener { _, _ ->
                it.onNext(getAsSortedList())
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