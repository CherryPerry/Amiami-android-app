package ru.cherryperry.amiami.domain.model

import java.util.regex.PatternSyntaxException

data class HighlightRule(
    override val id: Long = 0,
    val rule: String = "",
    val regex: Boolean = false
) : Model {

    private val regexValue: Regex? = if (regex) {
        try {
            Regex(rule, RegexOption.IGNORE_CASE)
        } catch (exception: PatternSyntaxException) {
            null
        }
    } else {
        null
    }
    val invalidRegex: Boolean = regexValue == null && regex

    fun isItemHighlighted(item: Item) = isItemHighlighted(item.name)

    fun isItemHighlighted(itemName: String): Boolean =
        when {
            invalidRegex -> false
            regexValue != null -> itemName.contains(regexValue)
            else -> itemName.contains(other = rule, ignoreCase = true)
        }

    override fun toString() = rule
}
