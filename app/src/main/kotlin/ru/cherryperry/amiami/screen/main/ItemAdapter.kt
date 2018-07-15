package ru.cherryperry.amiami.screen.main

import android.content.Context
import android.support.annotation.ColorRes
import android.support.annotation.LayoutRes
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import ru.cherryperry.amiami.R
import ru.cherryperry.amiami.model.Item
import ru.cherryperry.amiami.model.Items
import ru.cherryperry.amiami.util.GlideApp
import ru.cherryperry.amiami.util.HighlightFilter
import ru.cherryperry.amiami.util.ViewDelegate
import java.text.DateFormat
import java.util.Date

class ItemAdapter(context: Context, @LayoutRes private val layout: Int) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        private val VH_ITEM = 0
        private val VH_HEADER = 1
    }

    var itemClick: ((item: Item) -> Unit)? = null

    private val layoutInflater: LayoutInflater
    private var items: Items? = null
    private var highlights: HighlightFilter? = null

    private var fullSpan = 1
    private val spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
        override fun getSpanSize(position: Int): Int {
            if (items == null) return 1
            if (items!!.getItem(position).item == null) return fullSpan
            return 1
        }
    }

    init {
        layoutInflater = LayoutInflater.from(context)
    }

    fun setItems(items: Items?) {
        this.items = items
        notifyDataSetChanged()
    }

    fun setHighlights(highlights: Collection<String>) {
        this.highlights = HighlightFilter(highlights)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        val p = items!!.getItem(position)
        if (p.item != null)
            return VH_ITEM
        else
            return VH_HEADER
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == VH_HEADER)
            return HeaderHolder(layoutInflater.inflate(R.layout.item_last_update_header, parent, false))
        return ViewHolder(layoutInflater.inflate(layout, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val p = items!!.getItem(position)
        if (holder is ViewHolder)
            holder.bind(p.item)
        else if (holder is HeaderHolder)
            holder.bindTime(p.time)
    }

    override fun getItemCount(): Int {
        if (items == null) return 0
        return items!!.size()
    }

    fun getSpanSizeLookup(fullSpan: Int): GridLayoutManager.SpanSizeLookup {
        this.fullSpan = fullSpan
        return spanSizeLookup
    }

    class HeaderHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView by ViewDelegate<TextView>(R.id.text)

        internal fun bindTime(time: Long) {
            textView.text = DateFormat.getDateTimeInstance().format(Date(time * 1000))
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val image by ViewDelegate<ImageView?>(R.id.image)
        private val name by ViewDelegate<TextView?>(R.id.name)
        private val discount by ViewDelegate<TextView?>(R.id.discount)
        private val price by ViewDelegate<TextView?>(R.id.price)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(item: Item?) {
            if (item == null) return

            if (highlights != null && highlights!!.isHighlighted(item))
                setBackgroundAndKeepPadding(itemView, R.color.colorAccent)
            else setBackgroundAndKeepPadding(itemView, R.color.transparent)

            image?.let {
                GlideApp.with(itemView)
                    .load(item.image)
                    .centerCrop()
                    .into(it)
            }

            name?.text = item.name
            discount?.text = item.discount
            price?.text = item.price
        }

        override fun onClick(v: View) {
            if (adapterPosition >= 0)
                itemClick?.invoke(items!!.getItem(adapterPosition).item!!)
        }

        private fun setBackgroundAndKeepPadding(view: View, @ColorRes background: Int) {
            val top = view.paddingTop
            val left = view.paddingLeft
            val right = view.paddingRight
            val bottom = view.paddingBottom

            view.setBackgroundResource(background)
            view.setPadding(left, top, right, bottom)
        }
    }
}
