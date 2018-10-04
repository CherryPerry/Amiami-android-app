package ru.cherryperry.amiami.domain.filter

sealed class FilterUpdateParams

data class MinFilterUpdateParams(
    val min: Int
) : FilterUpdateParams()

data class MaxFilterUpdateParams(
    val max: Int
) : FilterUpdateParams()

data class TermFilterUpdateParams(
    val term: String
) : FilterUpdateParams()

object ResetFilterUpdateParams : FilterUpdateParams()
