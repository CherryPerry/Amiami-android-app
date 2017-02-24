package ru.cherryperry.amiami.screen.main

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.google.firebase.crash.FirebaseCrash
import ru.cherryperry.amiami.AmiamiApplication
import ru.cherryperry.amiami.AppPrefs
import ru.cherryperry.amiami.model.ItemRepository
import ru.cherryperry.amiami.model.Items
import ru.cherryperry.amiami.util.HighlightFilter
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import java.io.IOException
import javax.inject.Inject

@InjectViewState
class MainPresenter : MvpPresenter<MainView>() {

    @Inject
    lateinit var itemRepository: ItemRepository

    @Inject
    lateinit var appPrefs: AppPrefs

    private var subscription: Subscription? = null

    init {
        AmiamiApplication.mainScreenComponent.inject(this)

        load()
    }

    fun load() {
        if (subscription != null) subscription!!.unsubscribe()
        subscription = itemRepository.items(true)
                .map { showHighlightOnly(it) }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { viewState.showData(MainView.STATE_LOADING, null) }
                .subscribe({ showItems(it) }, { showError(it) })
    }

    fun reapplyFilter() {
        if (subscription != null) subscription!!.unsubscribe()
        subscription = itemRepository.items(false)
                .map { showHighlightOnly(it) }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { viewState.showData(MainView.STATE_LOADING, null) }
                .subscribe({ showItems(it) }, { showError(it) })
    }

    private fun showItems(items: Items) {
        viewState.showData(MainView.STATE_DONE, items)
        viewState.showFilterEnabled(items.isFilterEnabled)
    }

    private fun showError(throwable: Throwable) {
        if (throwable !is IOException) {
            FirebaseCrash.report(throwable)
        }
        throwable.printStackTrace()
        val errorState = if (throwable is IOException) MainView.STATE_ERROR_NETWORK else MainView.STATE_ERROR_INTERNAL
        viewState.showData(errorState, null)
        viewState.showFilterEnabled(false)
    }

    private fun showHighlightOnly(items: Items): Items {
        if (!appPrefs.favoritesOnly) return items

        val highlightFilter = HighlightFilter(appPrefs.favoriteList)
        val newItems = Items()
        newItems.setFilter(items.getFilterMin(), items.getFilterMax(), items.getSearchTerm())
        for (i in 0..(items.size() - 1)) {
            val position = items.getItem(i)
            if (position.item != null && highlightFilter.isHighlighted(position.item))
                newItems.add(position.item)
        }
        newItems.calculate()
        return newItems
    }

    override fun onDestroy() {
        super.onDestroy()
        if (subscription != null) subscription!!.unsubscribe()
    }
}
