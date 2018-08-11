package ru.cherryperry.amiami.domain.model

data class HighlightConfiguration(
    val rules: List<HighlightRule>,
    val asFilter: Boolean
) {

    fun isItemHighlighted(item: Item) = rules.any { it.isItemHighlighted(item) }
}
