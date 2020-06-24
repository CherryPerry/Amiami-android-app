package ru.cherryperry.amiami.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.crashlytics.android.Crashlytics
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import ru.cherryperry.amiami.domain.items.ItemListResult
import ru.cherryperry.amiami.domain.items.ItemListUseCase
import ru.cherryperry.amiami.presentation.base.BaseViewModel
import java.io.IOException
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val itemListUseCase: ItemListUseCase
) : BaseViewModel() {

    private lateinit var screenLiveData: MutableLiveData<ScreenState>
    private val filterEnabledLiveData = MutableLiveData<Boolean>()
    private var itemSubscription = Disposables.disposed()

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
        itemSubscription.dispose()
        itemSubscription = itemListUseCase.run(Unit)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { screenLiveData.value = ScreenState(ScreenState.STATE_LOADING, emptyList()) }
            .subscribe({ showItems(it) }, { showError(it) })
        this += itemSubscription
    }

    private fun showItems(items: ItemListResult) {
        screenLiveData.value = ScreenState(ScreenState.STATE_DONE, items.flatList)
        filterEnabledLiveData.value = items.filterEnabled
    }

    private fun showError(throwable: Throwable) {
        if (throwable !is IOException) {
            Crashlytics.logException(throwable)
        }
        throwable.printStackTrace()
        val errorState = if (throwable is IOException) {
            ScreenState.STATE_ERROR_NETWORK
        } else {
            ScreenState.STATE_ERROR_INTERNAL
        }
        screenLiveData.value = ScreenState(errorState, emptyList())
        filterEnabledLiveData.value = false
    }
}
