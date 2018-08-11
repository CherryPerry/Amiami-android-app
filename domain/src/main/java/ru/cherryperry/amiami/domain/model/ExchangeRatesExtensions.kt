package ru.cherryperry.amiami.domain.model

fun Sequence<Item>.applyCurrencyChange(rates: ru.cherryperry.amiami.domain.model.ExchangeRates, targetRate: String) =
    this.map { rates.changePrice(it, targetRate) }
