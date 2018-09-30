package ru.cherryperry.amiami.presentation.highlight

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.processors.BehaviorProcessor
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

    private val headerProcessor = BehaviorProcessor.createDefault(
        HighlightHeaderItem(addItemAction = this::addItem, validateInputAction = this::validateItem))

    val list: LiveData<List<Model>> by lazy {
        val data = MutableLiveData<List<Model>>()
        this += Flowable.combineLatest(
            headerProcessor,
            highlightListUseCase.run(Unit)
                .map {
                    it
                        .asSequence()
                        .map { highlightRule ->
                            HighlightItem(
                                highlightRule,
                                { setInputFromItem(it.highlightRule) },
                                { deleteItem(it.highlightRule) })
                        }
                        .toList()
                },
            BiFunction<HighlightHeaderItem, List<HighlightItem>, List<Model>> { header, items ->
                val result = ArrayList<Model>(items.size + 1)
                result += header
                result += items
                result
            })
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ data.value = it }, { it.printStackTrace() })
        data
    }

    val toastError = SingleLiveEvent<Int>()
    val scrollUpEvent = SingleLiveEvent<Unit>()

    fun validateItem(item: CharSequence) = item.length >= RULE_MIN_LENGTH

    fun setInputFromItem(item: HighlightRule) {
        scrollUpEvent.value = Unit
        headerProcessor.offer(HighlightHeaderItem(item.rule, item.regex, this::addItem, this::validateItem))
    }

    fun addItem(item: CharSequence, regexp: Boolean) {
        this += highlightListAddUseCase.run(HighlightRule(rule = item.toString(), regex = regexp))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({}, { it.printStackTrace() })
    }

    fun deleteItem(item: HighlightRule) {
        this += highlightListRemoveUseCase.run(item.id)
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
