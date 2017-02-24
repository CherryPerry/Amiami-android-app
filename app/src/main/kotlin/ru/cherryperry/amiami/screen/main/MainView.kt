package ru.cherryperry.amiami.screen.main

import android.support.annotation.IntDef
import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import ru.cherryperry.amiami.model.Items

interface MainView : MvpView {
    companion object {
        const val STATE_LOADING = 1L
        const val STATE_DONE = 2L
        const val STATE_ERROR_INTERNAL = 3L
        const val STATE_ERROR_NETWORK = 3L
    }

    @StateStrategyType(value = AddToEndSingleStrategy::class)
    fun showData(@MainViewState state: Long, itemList: Items?)

    @StateStrategyType(value = AddToEndSingleStrategy::class)
    fun showFilterEnabled(filterEnabled: Boolean)

    @Retention(AnnotationRetention.SOURCE)
    @IntDef(STATE_LOADING, STATE_DONE, STATE_ERROR_INTERNAL, STATE_ERROR_NETWORK)
    annotation class MainViewState
}
