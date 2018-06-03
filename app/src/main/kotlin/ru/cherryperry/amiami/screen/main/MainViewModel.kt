package ru.cherryperry.amiami.screen.main

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.google.firebase.crash.FirebaseCrash
import ru.cherryperry.amiami.AppPrefs
import ru.cherryperry.amiami.model.ItemRepository
import ru.cherryperry.amiami.model.Items
import ru.cherryperry.amiami.screen.base.BaseViewModel
import ru.cherryperry.amiami.util.HighlightFilter
import rx.android.schedulers.AndroidSchedulers
import rx.subscriptions.Subscriptions
import java.io.IOException
import javax.inject.Inject

class MainViewModel @Inject constructor(
        private val itemRepository: ItemRepository,
        private val appPrefs: AppPrefs
) : BaseViewModel() {

    private lateinit var screenLiveData: MutableLiveData<ScreenState>
    private val filterEnabledLiveData = MutableLiveData<Boolean>()
    private var itemSubscription = Subscriptions.unsubscribed()

    val screenState: LiveData<ScreenState>
        get() {
            if (!::screenLiveData.isInitialized) {
                screenLiveData = MutableLiveData()
                load()
            }
            return screenLiveData
        }

    val filterEnabled: LiveData<Boolean> = filterEnabledLiveData

    fun load() {
        itemSubscription.unsubscribe()
        itemSubscription = itemRepository.items(true)
                .map { showHighlightOnly(it) }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { screenLiveData.value = ScreenState(ScreenState.STATE_LOADING, null) }
                .subscribe({ showItems(it) }, { showError(it) })
        this += itemSubscription
    }

    fun reapplyFilter() {
        itemSubscription.unsubscribe()
        itemSubscription = itemRepository.items(false)
                .map { showHighlightOnly(it) }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { screenLiveData.value = ScreenState(ScreenState.STATE_LOADING, null) }
                .subscribe({ showItems(it) }, { showError(it) })
        this += itemSubscription
    }

    private fun showItems(items: Items) {
        screenLiveData.value = ScreenState(ScreenState.STATE_DONE, items)
        filterEnabledLiveData.value = items.isFilterEnabled
    }

    private fun showError(throwable: Throwable) {
        if (throwable !is IOException) {
            FirebaseCrash.report(throwable)
        }
        throwable.printStackTrace()
        val errorState = if (throwable is IOException) ScreenState.STATE_ERROR_NETWORK else ScreenState.STATE_ERROR_INTERNAL
        screenLiveData.value = ScreenState(errorState, null)
        filterEnabledLiveData.value = false
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
}