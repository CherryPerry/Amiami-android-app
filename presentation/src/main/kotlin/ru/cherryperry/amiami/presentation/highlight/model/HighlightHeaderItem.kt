package ru.cherryperry.amiami.presentation.highlight.model

import ru.cherryperry.amiami.domain.model.Model

class HighlightHeaderItem(
    val addItemAction: (CharSequence) -> Unit,
    val validateInputAction: (CharSequence) -> Boolean
) : Model {

    override val id: Long
        get() = 0

    override fun hashCode(): Int = HighlightHeaderItem::class.hashCode()

    override fun equals(other: Any?) = other is HighlightHeaderItem
}
