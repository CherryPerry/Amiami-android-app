package ru.cherryperry.amiami.screen.highlight

import android.content.SharedPreferences
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
    private val observer = SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key -> updateData() }

    init {
        highlightScreenComponent.inject(this)
        updateData()
        appPrefs.preferences.registerOnSharedPreferenceChangeListener(observer)
    }

    constructor() : this(AmiamiApplication.highlightScreenComponent)

    fun deleteItem(index: Int) {
        items.removeAt(index)
        appPrefs.favoriteList = TreeSet(items)
    }

    fun addItem(item: String) {
        items.add(item)
        appPrefs.favoriteList = TreeSet(items)
    }

    private fun updateData() {
        items.clear()
        items.addAll(ArrayList(appPrefs.favoriteList))
        items.sort()
        viewState.showData(items)
    }
}
