package ru.cherryperry.amiami.presentation.settings

import android.content.Context
import android.preference.PreferenceManager
import io.reactivex.Flowable
import ru.cherryperry.amiami.R
import ru.cherryperry.amiami.data.prefs.StringPreference
import javax.inject.Inject

/**
 * ListPreference does not work with integer values, only strings.
 * That is why different string preference was created.
 * Observing this state, we must call change original value method.
 */
class PushCounterFilterStringObserver @Inject constructor(
    context: Context
) {

    private val preferences = PreferenceManager.getDefaultSharedPreferences(context)
    private val stringPreference = StringPreference(context.getString(R.string.key_push_counter_filter_string),
        "0", preferences)

    fun observeStringPreference(): Flowable<String> = stringPreference.observer
}
