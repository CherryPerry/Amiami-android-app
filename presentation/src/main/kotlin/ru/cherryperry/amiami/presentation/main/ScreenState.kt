package ru.cherryperry.amiami.presentation.main

import androidx.annotation.IntDef
import ru.cherryperry.amiami.domain.model.Model

data class ScreenState(
    @MainViewState val state: Int,
    val list: List<Model> = emptyList()
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
