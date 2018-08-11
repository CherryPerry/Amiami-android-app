package ru.cherryperry.amiami.domain.model

data class Item(
    val url: String,
    val image: String,
    val name: String,
    val price: Price,
    val discount: String,
    val time: Long,
    val highlight: Boolean = false
) : Model {

    override val id: Long = url.hashCode().toLong()
}
