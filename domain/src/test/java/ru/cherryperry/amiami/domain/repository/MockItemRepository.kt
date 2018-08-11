package ru.cherryperry.amiami.domain.repository

import ru.cherryperry.amiami.domain.model.Item
import rx.Single

open class MockItemRepository : ItemRepository {

    override fun items(): Single<List<Item>> {
        throw NotImplementedError()
    }
}
