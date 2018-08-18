package ru.cherryperry.amiami.presentation.highlight

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import ru.cherryperry.amiami.R
import ru.cherryperry.amiami.domain.export.ExportHighlightUseCase
import ru.cherryperry.amiami.domain.export.ImportHighlightUseCase
import ru.cherryperry.amiami.domain.highlight.HighlightListAddUseCase
import ru.cherryperry.amiami.domain.highlight.HighlightListRemoveUseCase
import ru.cherryperry.amiami.domain.highlight.HighlightListUseCase
import ru.cherryperry.amiami.domain.model.HighlightRule
import ru.cherryperry.amiami.domain.model.Model
import ru.cherryperry.amiami.presentation.base.BaseViewModel
import ru.cherryperry.amiami.presentation.base.SingleLiveEvent
import ru.cherryperry.amiami.presentation.highlight.model.HighlightHeaderItem
import ru.cherryperry.amiami.presentation.highlight.model.HighlightItem
import rx.android.schedulers.AndroidSchedulers
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

class HighlightViewModel @Inject constructor(
    private val highlightListUseCase: HighlightListUseCase,
    private val highlightListAddUseCase: HighlightListAddUseCase,
    private val highlightListRemoveUseCase: HighlightListRemoveUseCase,
    private val exportHighlightUseCase: ExportHighlightUseCase,
    private val importHighlightUseCase: ImportHighlightUseCase
) : BaseViewModel() {

    companion object {
        private const val RULE_MIN_LENGTH = 3
    }

    val list: LiveData<List<Model>> by lazy {
        val data = MutableLiveData<List<Model>>()
        this += highlightListUseCase.run(Unit)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                data.value = result
                    .asSequence()
                    .map { highlightRule -> HighlightItem(highlightRule, this::deleteItem) }
                    .toMutableList<Model>()
                    .let {
                        it.add(0, HighlightHeaderItem(this::addItem, this::validateItem))
                        it
                    }
            }, { it.printStackTrace() })
        data
    }

    val toastError = SingleLiveEvent<Int>()

    fun validateItem(item: CharSequence) = item.length >= RULE_MIN_LENGTH

    fun addItem(item: CharSequence) {
        this += highlightListAddUseCase.run(item.toString())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({}, { it.printStackTrace() })
    }

    fun deleteItem(item: HighlightRule) {
        this += highlightListRemoveUseCase.run(item.rule)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({}, { it.printStackTrace() })
    }

    fun exportRules(outputStream: OutputStream) {
        this += exportHighlightUseCase.run(outputStream)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({}, { toastError.value = R.string.export_error })
    }

    fun importRules(inputStream: InputStream) {
        this += importHighlightUseCase.run(inputStream)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({}, { toastError.value = R.string.import_error })
    }
}
