package ru.cherryperry.amiami.domain.items

import ru.cherryperry.amiami.domain.model.Model

data class ItemListResult(
    val flatList: List<Model> = emptyList(),
    val filterEnabled: Boolean = false
)
