package ru.cherryperry.amiami.data.mapping

import ru.cherryperry.amiami.data.network.server.RemoteItem
import ru.cherryperry.amiami.domain.model.Item
import ru.cherryperry.amiami.mapping.Mapping

class JsonItemToItemMapping : Mapping<RemoteItem, Item> {

    private val jsonPriceToPriceMapping = JsonPriceToPriceMapping()

    override fun map(from: RemoteItem): Item = Item(from.url, from.image, from.name,
        jsonPriceToPriceMapping.map(from.price), from.discount, from.time)
}
