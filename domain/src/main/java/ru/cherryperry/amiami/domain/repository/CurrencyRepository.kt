package ru.cherryperry.amiami.domain.repository

import io.reactivex.Flowable
import io.reactivex.Single
import ru.cherryperry.amiami.domain.model.ExchangeRates

interface CurrencyRepository {

    fun currency(): Single<ExchangeRates>

    fun selectedCurrency(): Flowable<String>
}
