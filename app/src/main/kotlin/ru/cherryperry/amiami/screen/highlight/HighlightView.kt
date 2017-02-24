package ru.cherryperry.amiami.screen.highlight

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

interface HighlightView : MvpView {
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun showData(itemList: List<String>)
}
