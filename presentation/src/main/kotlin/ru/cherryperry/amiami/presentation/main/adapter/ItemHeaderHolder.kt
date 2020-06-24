package ru.cherryperry.amiami.presentation.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.cherryperry.amiami.R
import ru.cherryperry.amiami.presentation.util.ViewDelegate
import java.text.DateFormat
import java.util.Date
import java.util.concurrent.TimeUnit

class ItemHeaderHolder(
    viewGroup: ViewGroup
) : RecyclerView.ViewHolder(
    LayoutInflater.from(viewGroup.context).inflate(R.layout.main_item_header, viewGroup, false)
) {

    private val textView by ViewDelegate<TextView>(R.id.text)

    fun bindTime(time: Long) {
        textView.text = DateFormat.getDateTimeInstance().format(Date(TimeUnit.SECONDS.toMillis(time)))
    }
}
