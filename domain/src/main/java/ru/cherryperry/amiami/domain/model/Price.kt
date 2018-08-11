package ru.cherryperry.amiami.domain.model

import java.util.Locale

data class Price(
    val value: Double = 0.0,
    val currency: String = ExchangeRates.DEFAULT
) {

    override fun toString(): String =
        if (currency == ExchangeRates.DEFAULT) {
            String.format(Locale.getDefault(), "%.0f %s", value, currency)
        } else {
            String.format(Locale.getDefault(), "%.2f %s", value, currency)
        }
}
