package ru.cherryperry.amiami.presentation.filter

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.CoordinatorLayout
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import dagger.android.support.DaggerFragment
import ru.cherryperry.amiami.R
import ru.cherryperry.amiami.domain.model.Filter
import ru.cherryperry.amiami.presentation.base.NotNullObserver
import ru.cherryperry.amiami.presentation.util.ViewDelegate
import ru.cherryperry.amiami.presentation.util.ViewDelegateReset
import javax.inject.Inject

class FilterFragment : DaggerFragment(), SeekBar.OnSeekBarChangeListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: FilterViewModel

    private val viewDelegateReset = ViewDelegateReset()
    private val searchEdit by ViewDelegate<EditText>(R.id.search, viewDelegateReset)
    private val minSeekBar by ViewDelegate<SeekBar>(R.id.min, viewDelegateReset)
    private val maxSeekBar by ViewDelegate<SeekBar>(R.id.max, viewDelegateReset)
    private val minValue by ViewDelegate<TextView>(R.id.minValue, viewDelegateReset)
    private val maxValue by ViewDelegate<TextView>(R.id.maxValue, viewDelegateReset)

    private val minValueObserver = NotNullObserver<Int> { minSeekBar.progress = it }
    private val maxValueObserver = NotNullObserver<Int> { maxSeekBar.progress = it }

    // edit text must lose it's focus, when filter is hidden
    private val bottomSheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {

        override fun onSlide(bottomSheet: View, slideOffset: Float) {}

        override fun onStateChanged(bottomSheet: View, @BottomSheetBehavior.State newState: Int) {
            if (newState != BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheet.findFocus()?.also {
                    (context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                        .hideSoftInputFromWindow(it.windowToken, 0)
                    it.clearFocus()
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.filter, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(FilterViewModel::class.java)

        minSeekBar.max = Filter.LIMIT
        maxSeekBar.max = Filter.LIMIT + 1

        viewModel.searchMin.observe(this, NotNullObserver {
            minValue.text = context?.run { getString(R.string.yen_price_format, it) }
        })
        viewModel.searchMax.observe(this, NotNullObserver {
            maxValue.text = context?.run {
                if (it == Integer.MAX_VALUE) {
                    getString(R.string.no_limit)
                } else {
                    getString(R.string.yen_price_format, it)
                }
            }
        })
        viewModel.searchTerm.observe(this, NotNullObserver {
            if (searchEdit.text.toString() != it) {
                val selectionStart = searchEdit.selectionStart
                val selectionEnd = searchEdit.selectionEnd
                searchEdit.setText(it)
                searchEdit.setSelection(selectionStart, selectionEnd)
            }
        })
        searchEdit.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(editable: Editable) {
                viewModel.setTerm(editable.toString())
            }

            override fun beforeTextChanged(sequence: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(sequence: CharSequence, start: Int, before: Int, count: Int) {}
        })

        minSeekBar.setOnSeekBarChangeListener(this)
        maxSeekBar.setOnSeekBarChangeListener(this)
        observeSeekBar(minSeekBar)
        observeSeekBar(maxSeekBar)

        // click on header must expand filter view
        view.findViewById<View>(R.id.header).setOnClickListener { _ ->
            if (view.layoutParams is CoordinatorLayout.LayoutParams) {
                BottomSheetBehavior.from(view).state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewDelegateReset.onDestroyView()
    }

    override fun onStart() {
        super.onStart()
        registerFocusLossOnHide()
    }

    override fun onStop() {
        super.onStop()
        unregisterFocusLossOnHide()
    }

    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        if (!fromUser) {
            return
        }
        when (seekBar) {
            minSeekBar -> viewModel.setMin(Math.min(progress, Filter.LIMIT))
            maxSeekBar -> viewModel.setMax(if (progress == seekBar.max) Int.MAX_VALUE else progress)
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {
        stopObservingSeekBar(seekBar)
    }

    override fun onStopTrackingTouch(seekBar: SeekBar) {
        observeSeekBar(seekBar)
    }

    private fun observeSeekBar(seekBar: SeekBar) {
        when (seekBar) {
            minSeekBar -> viewModel.searchMin.observe(this, minValueObserver)
            maxSeekBar -> viewModel.searchMax.observe(this, maxValueObserver)
        }
    }

    private fun stopObservingSeekBar(seekBar: SeekBar) {
        when (seekBar) {
            minSeekBar -> viewModel.searchMin.removeObserver(minValueObserver)
            maxSeekBar -> viewModel.searchMax.removeObserver(maxValueObserver)
        }
    }

    private fun registerFocusLossOnHide() {
        view?.also {
            val behavior = BottomSheetBehavior.from(it)
            behavior.setBottomSheetCallback(bottomSheetCallback)
        }
    }

    private fun unregisterFocusLossOnHide() {
        view?.also {
            val behavior = BottomSheetBehavior.from(it)
            behavior.setBottomSheetCallback(null)
        }
    }
}
