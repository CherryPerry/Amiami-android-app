package ru.cherryperry.amiami.domain.items

import ru.cherryperry.amiami.domain.model.Filter
import ru.cherryperry.amiami.domain.model.Model

data class ItemListResult(
    val flatList: List<Model>,
    val filter: Filter
)
