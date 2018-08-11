package ru.cherryperry.amiami.presentation.highlight.model

import ru.cherryperry.amiami.domain.model.HighlightRule
import ru.cherryperry.amiami.domain.model.Model

class HighlightItem(
    val highlightRule: HighlightRule,
    val removeItemAction: (HighlightRule) -> Unit
) : Model {

    override val id: Long
        get() = highlightRule.id

    override fun equals(other: Any?) = other is HighlightItem &&
        highlightRule == other.highlightRule

    override fun hashCode() = highlightRule.hashCode()

    override fun toString() = highlightRule.toString()
}
