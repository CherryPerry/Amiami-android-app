package ru.cherryperry.amiami.screen.main

import android.support.annotation.IntDef
import ru.cherryperry.amiami.model.Items

data class ScreenState(
        @MainViewState val state: Int,
        val itemList: Items?
) {
    companion object {
        const val STATE_LOADING = 1
        const val STATE_DONE = 2
        const val STATE_ERROR_INTERNAL = 3
        const val STATE_ERROR_NETWORK = 4
    }

    @Retention(AnnotationRetention.SOURCE)
    @IntDef(STATE_LOADING, STATE_DONE, STATE_ERROR_INTERNAL, STATE_ERROR_NETWORK)
    annotation class MainViewState
}