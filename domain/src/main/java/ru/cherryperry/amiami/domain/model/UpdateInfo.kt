package ru.cherryperry.amiami.domain.model

/**
 * Information about application update on [https://github.com].
 */
data class UpdateInfo(
    val version: Version,
    val name: String,
    val url: String
)
