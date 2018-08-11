package ru.cherryperry.amiami.data.mapping

import org.junit.Assert
import org.junit.Test
import ru.cherryperry.amiami.data.network.server.RemoteItem
import ru.cherryperry.amiami.domain.model.Item
import ru.cherryperry.amiami.domain.model.Price

class JsonItemToItemMappingTest {

    private val jsonItemToItemMapping = JsonItemToItemMapping()

    @Test
    fun testFieldsMappedCorrectly() {
        val remoteItem = RemoteItem("url", "image", "name", "30%", "2,000 yen", 1)
        Assert.assertEquals(
            Item(remoteItem.url, remoteItem.image, remoteItem.name, Price(2000.0), remoteItem.discount, remoteItem.time),
            jsonItemToItemMapping.map(remoteItem)
        )
    }
}
