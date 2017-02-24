package ru.cherryperry.amiami.screen.main

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.SeekBar
import android.widget.TextView
import com.jakewharton.rxbinding.widget.RxTextView
import ru.cherryperry.amiami.AppPrefs
import ru.cherryperry.amiami.R
import ru.cherryperry.amiami.util.ViewDelegate
import rx.android.schedulers.AndroidSchedulers
import rx.subjects.BehaviorSubject
import java.util.concurrent.TimeUnit

/**
 * Вьюха для контроля bottom sheet bar'a
 */
class FilterView : FrameLayout, SeekBar.OnSeekBarChangeListener {
    // View fields
    private val searchEdit by ViewDelegate<EditText>(R.id.search)
    private val minSeekBar by ViewDelegate<SeekBar>(R.id.min)
    private val maxSeekBar by ViewDelegate<SeekBar>(R.id.max)
    private val minValue by ViewDelegate<TextView>(R.id.minValue)
    private val maxValue by ViewDelegate<TextView>(R.id.maxValue)

    // Private fields
    private lateinit var appPrefs: AppPrefs
    private val seekBarCallbackDebouncer = BehaviorSubject.create<Pair<Int, Int>>()

    // Callbacks
    var onSeekBarChange: ((min: Int, max: Int) -> Unit)? = null
    var onSearchTextChange: ((search: String?) -> Unit)? = null
    var onRequestShowSheet: (() -> Unit)? = null

    constructor(ctx: Context) : super(ctx) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        LayoutInflater.from(context).inflate(R.layout.layout_bottom, this, true)

        getChildAt(0).setOnClickListener { onRequestShowSheet?.invoke() }

        if (!isInEditMode) {
            appPrefs = AppPrefs(context)

            maxSeekBar.progress = appPrefs.priceMax
            minSeekBar.progress = appPrefs.priceMin

            minSeekBar.setOnSeekBarChangeListener(this)
            maxSeekBar.setOnSeekBarChangeListener(this)

            searchEdit.setText(appPrefs.searchTerm)

            // Update text values
            onProgressChanged(minSeekBar, minSeekBar.progress, false)

            // Debounce search
            RxTextView.afterTextChangeEvents(searchEdit)
                    .onBackpressureLatest()
                    .debounce(300, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        val test: String = it.editable().toString()
                        appPrefs.searchTerm = test
                        onSearchTextChange?.invoke(test)
                    }, Throwable::printStackTrace)

            // Debounce slider values
            seekBarCallbackDebouncer
                    .onBackpressureLatest()
                    .debounce(300, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ onSeekBarChange?.invoke(it.first, it.second) }, Throwable::printStackTrace)
        }
    }

    override fun onProgressChanged(seekBar: SeekBar, value: Int, user: Boolean) {
        when (seekBar.id) {
            R.id.min -> if (value >= maxSeekBar.progress) maxSeekBar.progress = value + 1
            R.id.max -> if (value < minSeekBar.progress) minSeekBar.progress = value
        }

        var max = maxSeekBar.progress
        if (max == maxSeekBar.max) max = Integer.MAX_VALUE
        val min = minSeekBar.progress

        minValue.text = context.getString(R.string.yen_price_format, min)
        maxValue.text = if (max == Integer.MAX_VALUE) context.getString(R.string.no_limit) else context.getString(R.string.yen_price_format, max)

        appPrefs.priceMin = min
        appPrefs.priceMax = max

        if (user) {
            seekBarCallbackDebouncer.onNext(Pair(min, max))
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar) {
    }

    fun getSearchTerm() = searchEdit.text.toString()

    fun getMaxValue() = appPrefs.priceMax

    fun getMinValue() = appPrefs.priceMin
}