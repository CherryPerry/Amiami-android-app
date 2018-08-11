package ru.cherryperry.amiami.domain.model

fun Sequence<Item>.applyHighlightConfiguration(configuration: HighlightConfiguration) =
    if (configuration.asFilter) {
        this.filter { configuration.isItemHighlighted(it) }
    } else {
        this.map { if (configuration.isItemHighlighted(it)) it.copy(highlight = true) else it }
    }
