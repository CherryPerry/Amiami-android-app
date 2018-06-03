package ru.cherryperry.amiami.screen.highlight

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.jakewharton.rxbinding.view.RxView
import com.jakewharton.rxbinding.widget.RxTextView
import dagger.android.support.DaggerAppCompatActivity
import ru.cherryperry.amiami.R
import ru.cherryperry.amiami.util.SAFSettingsProvider
import ru.cherryperry.amiami.util.ViewDelegate
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject

/**
 * Highlight list activity
 */
class HighlightActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var highlightViewModel: HighlightViewModel

    private val recyclerView by ViewDelegate<RecyclerView>(R.id.recyclerView)
    private val addButton by ViewDelegate<Button>(R.id.add)
    private val editText by ViewDelegate<EditText>(R.id.edit)

    private lateinit var adapter: HighlightAdapter
    private val compositeSubscription: CompositeSubscription = CompositeSubscription()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        highlightViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(HighlightViewModel::class.java)

        setContentView(R.layout.activity_highlight)

        // Add button
        compositeSubscription.add(
                RxTextView.textChanges(editText).subscribe { sequence -> addButton.isEnabled = sequence.length > 3 })
        compositeSubscription.add(
                RxView.clicks(addButton).subscribe {
                    highlightViewModel.addItem(editText.text.toString())
                    editText.text.clear()
                }
        )

        // RecyclerView
        adapter = HighlightAdapter()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // Swipe helper
        val helper = ItemTouchHelper(object : ItemTouchHelper.Callback() {
            override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
                val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
                return makeMovementFlags(0, swipeFlags)
            }

            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val item = adapter.getItem(viewHolder.adapterPosition)
                highlightViewModel.deleteItem(item)
            }

            override fun isLongPressDragEnabled(): Boolean {
                return false
            }

            override fun isItemViewSwipeEnabled(): Boolean {
                return true
            }
        })
        helper.attachToRecyclerView(recyclerView)

        highlightViewModel.list.observe(this, Observer { it?.let { showData(it) } })
    }

    override fun onDestroy() {
        compositeSubscription.clear()
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (SAFSettingsProvider.isAvailable()) {
            menuInflater.inflate(R.menu.highlight_menu, menu)
        }
        return super.onCreateOptionsMenu(menu)
    }

    @SuppressLint("NewApi")
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (SAFSettingsProvider.isAvailable()) {
            item?.let {
                when (it.itemId) {
                    R.id.action_export -> SAFSettingsProvider.requestCreateDocument(this)
                    R.id.action_import -> SAFSettingsProvider.requestOpenDocument(this)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("NewApi")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (SAFSettingsProvider.isAvailable()) {
            if (SAFSettingsProvider.onRequestCreateDocumentComplete(this, requestCode, resultCode, data) == SAFSettingsProvider.Result.Error) {
                Toast.makeText(this, R.string.export_error, Toast.LENGTH_SHORT).show()
            }
            if (SAFSettingsProvider.onRequestOpenDocumentComplete(this, requestCode, resultCode, data) == SAFSettingsProvider.Result.Error) {
                Toast.makeText(this, R.string.import_error, Toast.LENGTH_SHORT).show()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun showData(itemList: List<String>) {
        adapter.setItems(itemList)
    }
}
