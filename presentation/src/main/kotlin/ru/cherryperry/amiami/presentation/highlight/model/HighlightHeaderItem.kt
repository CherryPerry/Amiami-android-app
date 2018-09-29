package ru.cherryperry.amiami.presentation.highlight.model

import ru.cherryperry.amiami.domain.model.HighlightRule
import ru.cherryperry.amiami.domain.model.Model

class HighlightHeaderItem(
    val addItemAction: (CharSequence, Boolean) -> Unit,
    val validateInputAction: (CharSequence) -> Boolean,
    val initialData: HighlightRule? = null
) : Model {

    override val id: Long = 0

    override fun hashCode(): Int = HighlightHeaderItem::class.hashCode()

    override fun equals(other: Any?) = this === other
}
