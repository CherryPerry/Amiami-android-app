package ru.cherryperry.amiami.presentation.highlight.model

import ru.cherryperry.amiami.domain.model.Model

class HighlightHeaderItem(
    var input: String = "",
    var regex: Boolean = false,
    val addItemAction: (CharSequence, Boolean) -> Unit,
    val validateInputAction: (CharSequence) -> Boolean
) : Model {

    override val id: Long = 0

    override fun hashCode(): Int = input.hashCode() xor regex.hashCode()

    override fun equals(other: Any?) =
        other is HighlightHeaderItem
            && other.input == input
            && other.regex == regex
}
