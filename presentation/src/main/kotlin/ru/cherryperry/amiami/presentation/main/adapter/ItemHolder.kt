package ru.cherryperry.amiami.presentation.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.TooltipCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import ru.cherryperry.amiami.R
import ru.cherryperry.amiami.domain.model.Item
import ru.cherryperry.amiami.presentation.util.GlideApp
import ru.cherryperry.amiami.presentation.util.ViewDelegate

class ItemHolder(
    viewGroup: ViewGroup,
    showName: Boolean,
    private val onItemClick: (Int) -> Unit
) : RecyclerView.ViewHolder(
    LayoutInflater.from(viewGroup.context).inflate(
        if (showName) {
            R.layout.main_item_card_big
        } else {
            R.layout.main_item_card_small
        }, viewGroup,
        false)
), View.OnClickListener {

    private val imageView by ViewDelegate<ImageView>(R.id.image)
    private val nameView by ViewDelegate<TextView?>(R.id.name)
    private val priceView by ViewDelegate<TextView>(R.id.price)

    init {
        itemView.setOnClickListener(this)
    }

    fun bind(item: Item) {
        itemView.isActivated = item.highlight
        GlideApp.with(itemView)
            .load(item.image)
            .placeholder(R.drawable.placeholder_image)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(imageView)
        val nameView = this.nameView
        if (nameView != null) {
            nameView.text = item.name
            nameView.isActivated = item.highlight
        } else {
            TooltipCompat.setTooltipText(itemView, item.name)
        }
        priceView.text = item.price.toString()
    }

    override fun onClick(view: View) {
        if (adapterPosition >= 0) {
            onItemClick(adapterPosition)
        }
    }
}
