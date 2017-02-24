package ru.cherryperry.amiami.model

import java.util.*

class ItemGroup(val time: Long) {
    val items: MutableList<Item>

    val iterator: Iterator<Item>
        get() = items.iterator()

    init {
        items = ArrayList<Item>(128)
    }

    fun sort() {
        items.sortBy(Item::url)
    }
}
