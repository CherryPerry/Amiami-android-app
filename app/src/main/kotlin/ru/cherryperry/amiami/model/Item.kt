package ru.cherryperry.amiami.model

import ru.cherryperry.amiami.data.network.server.RemoteItem
import java.util.regex.Pattern
import kotlin.properties.Delegates

class Item {
    companion object {
        private val pricePattern = Pattern.compile("\\d+(,\\d+)?")
    }

    var url: String? = null
    var image: String? = null
    var name: String? = null
    var discount: String? = null
    var time: Long = 0

    private var priceInt = -1
    var price by Delegates.observable(null as String?) { prop, old, new ->
        if (new != null && pricePattern.matcher(new).find()) {
            val cost = new.replace("\\D+".toRegex(), "")
            try {
                priceInt = cost.toInt()
                return@observable
            } catch (e: NumberFormatException) {
            }
        }
        priceInt = -1
    }

    constructor(item: RemoteItem) {
        url = item.url
        image = item.image
        name = item.name
        discount = item.discount
        price = item.price
        time = item.time
    }

    /**
     * Test constructor
     */
    constructor()

    fun isPriceInRange(min: Int, max: Int): Boolean {
        val p = getPriceInt()
        return p != null && p >= min && p <= max
    }

    fun isContainsTerm(term: String?): Boolean {
        if (term == null || term.isEmpty()) return true
        name ?: return false
        return name!!.contains(other = term, ignoreCase = true)
    }

    fun getPriceInt(): Int? {
        return if (priceInt > 0) priceInt else null
    }

    fun copy(): Item {
        val i = Item()
        i.discount = discount
        i.image = image
        i.name = name
        i.price = price
        i.time = time
        i.url = url
        return i
    }
}
