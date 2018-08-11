package ru.cherryperry.amiami.domain.model

import java.util.regex.PatternSyntaxException

data class HighlightRule(
    val rule: String
) : Model {

    private val regex: Regex? = try {
        Regex(rule, RegexOption.IGNORE_CASE)
    } catch (exception: PatternSyntaxException) {
        null
    }

    override val id = rule.hashCode().toLong()

    fun isItemHighlighted(item: Item) = isItemHighlighted(item.name)

    fun isItemHighlighted(itemName: String) = regex?.let { itemName.contains(it) }
        ?: itemName.contains(other = rule, ignoreCase = true)

    override fun toString() = rule
}
