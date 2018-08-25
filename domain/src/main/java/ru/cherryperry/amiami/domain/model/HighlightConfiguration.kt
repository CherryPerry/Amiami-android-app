package ru.cherryperry.amiami.domain.model

data class HighlightConfiguration(
    val rules: List<HighlightRule> = emptyList(),
    val asFilter: Boolean = false
) {

    fun isItemHighlighted(item: Item) = rules.any { it.isItemHighlighted(item) }
}
