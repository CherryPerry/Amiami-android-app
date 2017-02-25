package ru.cherryperry.amiami.screen.update

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

interface UpdateView : MvpView {

    @StateStrategyType(value = OneExecutionStateStrategy::class)
    fun showUpdateAvailableDialog(tagName: String, name: String, url: String)
}