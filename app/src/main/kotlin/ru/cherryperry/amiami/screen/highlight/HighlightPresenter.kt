package ru.cherryperry.amiami.screen.highlight

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import ru.cherryperry.amiami.AmiamiApplication
import ru.cherryperry.amiami.AppPrefs
import java.util.*
import javax.inject.Inject

@InjectViewState
class HighlightPresenter(highlightScreenComponent: HighlightScreenComponent) : MvpPresenter<HighlightView>() {

    @Inject
    lateinit var appPrefs: AppPrefs

    private val items = ArrayList<String>()

    init {
        highlightScreenComponent.inject(this)
        items.addAll(ArrayList(appPrefs.favoriteList))
        items.sort()
    }

    constructor() : this(AmiamiApplication.highlightScreenComponent) {
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.showData(items)
    }

    fun deleteItem(index: Int) {
        items.removeAt(index)
        items.sort()
        appPrefs.favoriteList = TreeSet(items)
        viewState.showData(items)
    }

    fun addItem(item: String) {
        items.add(item)
        items.sort()
        appPrefs.favoriteList = TreeSet(items)
        viewState.showData(items)
    }
}
