package ru.cherryperry.amiami.screen.highlight

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import ru.cherryperry.amiami.AmiamiApplication
import ru.cherryperry.amiami.domain.highlight.*
import rx.android.schedulers.AndroidSchedulers
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject

/**
 * Highlight list presenter
 */
@InjectViewState
class HighlightPresenter(highlightScreenComponent: HighlightScreenComponent) : MvpPresenter<HighlightView>() {

    @Inject
    lateinit var highlightListUseCase: HighlightListUseCase

    @Inject
    lateinit var highlightListAddUseCase: HighlightListAddUseCase

    @Inject
    lateinit var highlightListRemoveUseCase: HighlightListRemoveUseCase

    private val compositeSubscription = CompositeSubscription()

    constructor() : this(AmiamiApplication.highlightScreenComponent)

    init {
        highlightScreenComponent.inject(this)
        // Subscribe to list and it's future changes
        compositeSubscription.add(
                highlightListUseCase.run(HighlightListUseCaseParams())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            viewState.showData(it.list)
                        }, {
                            it.printStackTrace()
                        }))
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeSubscription.clear()
    }

    /**
     * Delete item from list
     */
    fun deleteItem(item: String) {
        compositeSubscription.add(
                highlightListRemoveUseCase.run(HighlightListRemoveUseCaseParams(item))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({}, {
                            it.printStackTrace()
                        }))
    }

    /**
     * Add item to list
     */
    fun addItem(item: String) {
        compositeSubscription.add(
                highlightListAddUseCase.run(HighlightListAddUseCaseParams(item))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({}, {
                            it.printStackTrace()
                        }))
    }
}
