package ru.cherryperry.amiami.mapping

interface Mapping<From, To> {

    fun map(from: From): To
}
