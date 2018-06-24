package ru.cherryperry.amiami.screen.highlight

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import ru.cherryperry.amiami.domain.highlight.HighlightListAddUseCase
import ru.cherryperry.amiami.domain.highlight.HighlightListRemoveUseCase
import ru.cherryperry.amiami.domain.highlight.HighlightListUseCase
import ru.cherryperry.amiami.screen.base.BaseViewModel
import rx.android.schedulers.AndroidSchedulers
import javax.inject.Inject

class HighlightViewModel @Inject constructor(
        private val highlightListUseCase: HighlightListUseCase,
        private val highlightListAddUseCase: HighlightListAddUseCase,
        private val highlightListRemoveUseCase: HighlightListRemoveUseCase
) : BaseViewModel() {

    private lateinit var internalList: MutableLiveData<List<String>>
    val list: LiveData<List<String>>
        get() {
            if (!::internalList.isInitialized) {
                internalList = MutableLiveData()
                this += highlightListUseCase.run(Any())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ internalList.value = it }, { it.printStackTrace() })
            }
            return internalList
        }

    fun deleteItem(item: String) {
        this += highlightListRemoveUseCase.run(item)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({}, { it.printStackTrace() })
    }

    fun addItem(item: String) {
        this += highlightListAddUseCase.run(item)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({}, { it.printStackTrace() })
    }
}