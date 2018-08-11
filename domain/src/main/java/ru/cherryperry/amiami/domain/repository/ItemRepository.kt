package ru.cherryperry.amiami.domain.repository

import ru.cherryperry.amiami.domain.model.Item
import rx.Single

interface ItemRepository {

    fun items(): Single<List<Item>>
}
