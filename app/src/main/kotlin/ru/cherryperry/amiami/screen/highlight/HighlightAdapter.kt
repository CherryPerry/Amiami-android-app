package ru.cherryperry.amiami.screen.highlight

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import ru.cherryperry.amiami.R
import ru.cherryperry.amiami.util.ViewDelegate
import java.util.ArrayList

/**
 * Adapter for highlight list items of String type
 */
class HighlightAdapter : RecyclerView.Adapter<HighlightAdapter.FavoriteViewHolder>() {
    private val items: MutableList<String>

    init {
        items = ArrayList()
        setHasStableIds(true)
    }

    fun setItems(collection: List<String>) {
        val calculateDiff = DiffUtil.calculateDiff(object : DiffUtil.Callback() {

            override fun getOldListSize(): Int = items.size

            override fun getNewListSize(): Int = collection.size

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = collection[newItemPosition] == items[oldItemPosition]

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = true
        })
        items.clear()
        items.addAll(collection)
        calculateDiff.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder = FavoriteViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_highlight, parent, false))

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    override fun getItemId(position: Int): Long = getItem(position).hashCode().toLong()

    fun getItem(position: Int): String = items[position]

    class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView by ViewDelegate<TextView>(R.id.text)

        internal fun bind(text: String) {
            textView.text = text
        }
    }
}
