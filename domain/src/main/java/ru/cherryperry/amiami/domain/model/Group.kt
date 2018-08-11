package ru.cherryperry.amiami.domain.model

data class Group(
    val time: Long
) : Model {

    override val id: Long = time
}
