package ru.cherryperry.amiami.data.repository

import ru.cherryperry.amiami.data.mapping.JsonItemToItemMapping
import ru.cherryperry.amiami.data.network.server.ServerApi
import ru.cherryperry.amiami.domain.model.Item
import ru.cherryperry.amiami.domain.repository.ItemRepository
import rx.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ItemRepositoryImpl @Inject constructor(
    private val api: ServerApi
) : ItemRepository {

    private val jsonItemToItemMapping = JsonItemToItemMapping()

    override fun items(): Single<List<Item>> = api.items().map { it.map { jsonItemToItemMapping.map(it) } }
}
