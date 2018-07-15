package ru.cherryperry.amiami.model

import java.util.ArrayList

class ItemGroup(val time: Long) {
    val items: MutableList<Item>

    val iterator: Iterator<Item>
        get() = items.iterator()

    init {
        items = ArrayList(128)
    }

    fun sort() {
        items.sortBy(Item::url)
    }
}
