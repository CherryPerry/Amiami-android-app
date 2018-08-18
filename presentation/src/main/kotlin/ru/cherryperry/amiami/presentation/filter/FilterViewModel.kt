package ru.cherryperry.amiami.presentation.filter

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import ru.cherryperry.amiami.domain.filter.FilterGetUseCase
import ru.cherryperry.amiami.domain.filter.FilterUpdateParams
import ru.cherryperry.amiami.domain.filter.FilterUpdateUseCase
import ru.cherryperry.amiami.presentation.base.BaseViewModel
import rx.android.schedulers.AndroidSchedulers
import javax.inject.Inject

class FilterViewModel @Inject constructor(
    private val filterGetUseCase: FilterGetUseCase,
    private val filterUpdateUseCase: FilterUpdateUseCase
) : BaseViewModel() {

    private val searchTermLiveData = MutableLiveData<String>()
    private val searchMinLiveData = MutableLiveData<Int>()
    private val searchMaxLiveData = MutableLiveData<Int>()

    val searchTerm: LiveData<String>
        get() = searchTermLiveData
    val searchMin: LiveData<Int>
        get() = searchMinLiveData
    val searchMax: LiveData<Int>
        get() = searchMaxLiveData

    init {
        loadFilter()
    }

    private fun loadFilter() {
        this += filterGetUseCase.run(Unit)
            .onBackpressureLatest()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                searchTermLiveData.value = it.textFilter
                searchMinLiveData.value = it.minPrice
                searchMaxLiveData.value = it.maxPrice
            }, {
                it.printStackTrace()
            })
    }

    fun setTerm(value: String) {
        this += filterUpdateUseCase.run(FilterUpdateParams(term = value)).subscribe()
    }

    fun setMin(value: Int) {
        this += filterUpdateUseCase.run(FilterUpdateParams(min = value)).subscribe()
    }

    fun setMax(value: Int) {
        this += filterUpdateUseCase.run(FilterUpdateParams(max = value)).subscribe()
    }
}
