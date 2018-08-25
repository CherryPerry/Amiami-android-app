package ru.cherryperry.amiami.domain.filter

import io.reactivex.Completable

// TODO Make different classes!
data class FilterUpdateParams(
    val min: Int? = null,
    val max: Int? = null,
    val term: String? = null
) {

    init {
        if (min == null && max == null && term == null) {
            throw IllegalArgumentException("This update is meaningless")
        }
        if (min != null && max != null && min > max) {
            throw IllegalArgumentException("Min can't be higher than max")
        }
    }

    fun resolve(
        minAction: (Int) -> Completable,
        maxAction: (Int) -> Completable,
        termAction: (String) -> Completable
    ) = when {
        min != null -> minAction(min)
        max != null -> maxAction(max)
        term != null -> termAction(term)
        else -> throw IllegalStateException()
    }
}
