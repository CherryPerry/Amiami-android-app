package ru.cherryperry.amiami.domain.update

/**
 * Information about application update on github.com
 */
data class UpdateInfo(
    val tagName: String,
    val name: String,
    val url: String
)