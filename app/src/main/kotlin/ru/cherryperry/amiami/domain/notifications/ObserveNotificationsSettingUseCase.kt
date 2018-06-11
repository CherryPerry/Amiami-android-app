package ru.cherryperry.amiami.domain.notifications

import android.content.Context
import android.content.SharedPreferences
import ru.cherryperry.amiami.AppPrefs
import ru.cherryperry.amiami.R
import ru.cherryperry.amiami.domain.ObservableUseCase
import rx.Emitter
import rx.Observable
import javax.inject.Inject

class ObserveNotificationsSettingUseCase @Inject constructor(
        private val appPrefs: AppPrefs,
        private val context: Context
) : ObservableUseCase<Any, Boolean>() {

    override fun run(param: Any): Observable<Boolean> {
        val listener = Listener(appPrefs, context)
        return Observable
                .create<Boolean>(
                        { emitter ->
                            listener.emitter = emitter
                            appPrefs.preferences.registerOnSharedPreferenceChangeListener(listener)
                        }, Emitter.BackpressureMode.LATEST)
                .doOnUnsubscribe { appPrefs.preferences.unregisterOnSharedPreferenceChangeListener(listener) }
    }

    private class Listener constructor(
            private val appPrefs: AppPrefs,
            private val context: Context
    ) : SharedPreferences.OnSharedPreferenceChangeListener {

        @Volatile
        var emitter: Emitter<Boolean>? = null

        override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
            emitter?.apply {
                if (key == context.getString(R.string.key_push)) {
                    onNext(appPrefs.push)
                }
            }
        }
    }
}
