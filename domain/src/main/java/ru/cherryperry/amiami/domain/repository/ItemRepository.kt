package ru.cherryperry.amiami.domain.repository

import io.reactivex.Single
import ru.cherryperry.amiami.domain.model.Item

interface ItemRepository {

    fun items(): Single<List<Item>>
}
