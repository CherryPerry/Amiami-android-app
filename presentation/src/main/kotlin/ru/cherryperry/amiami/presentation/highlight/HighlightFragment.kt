package ru.cherryperry.amiami.presentation.highlight

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import ru.cherryperry.amiami.R
import ru.cherryperry.amiami.presentation.activity.Navigator
import ru.cherryperry.amiami.presentation.base.BaseFragment
import ru.cherryperry.amiami.presentation.base.NotNullObserver
import ru.cherryperry.amiami.presentation.highlight.adapter.HighlightAdapter
import ru.cherryperry.amiami.presentation.util.ViewDelegate
import ru.cherryperry.amiami.presentation.util.addPaddingBottomToFitBottomSystemInset
import javax.inject.Inject

class HighlightFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var navigator: Navigator

    private lateinit var highlightViewModel: HighlightViewModel

    private val recyclerView by ViewDelegate<RecyclerView>(R.id.recyclerView)
    private val toolbar by ViewDelegate<Toolbar>(R.id.toolbar)

    private val adapter = HighlightAdapter()
    private val safRuleExport = SafRuleExport()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.highlight, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        highlightViewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(HighlightViewModel::class.java)

        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(HighlightSeparatorItemDecoration(context!!))

        // Toolbar
        navigator.configureToolbar(toolbar)
        if (safRuleExport.isAvailable()) {
            toolbar.inflateMenu(R.menu.highlight_menu)
            toolbar.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.action_export -> safRuleExport.requestCreateDocument(this)
                    R.id.action_import -> safRuleExport.requestOpenDocument(this)
                }
                true
            }
        }

        highlightViewModel.list.observe(viewLifecycleOwner, NotNullObserver { adapter.submitList(it) })
        highlightViewModel.toastError.observe(viewLifecycleOwner, NotNullObserver {
            Toast.makeText(context!!, it, Toast.LENGTH_SHORT).show()
        })
        highlightViewModel.scrollUpEvent.observe(viewLifecycleOwner, NotNullObserver {
            recyclerView.smoothScrollToPosition(0)
        })

        addPaddingBottomToFitBottomSystemInset(recyclerView)
        ViewCompat.requestApplyInsets(view)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (safRuleExport.isAvailable()) {
            safRuleExport.onRequestCreateDocumentComplete(context!!, requestCode, resultCode, data!!,
                highlightViewModel::exportRules)
            safRuleExport.onRequestOpenDocumentComplete(context!!, requestCode, resultCode, data!!,
                highlightViewModel::importRules)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

}
