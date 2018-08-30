package ru.cherryperry.amiami.domain.model

fun Sequence<Item>.applyCurrencyChange(rates: ExchangeRates, targetRate: String) =
    this.map { rates.changePrice(it, targetRate) }
