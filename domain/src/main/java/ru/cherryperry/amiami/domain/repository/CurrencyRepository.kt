package ru.cherryperry.amiami.domain.repository

import ru.cherryperry.amiami.domain.model.ExchangeRates
import rx.Observable
import rx.Single

interface CurrencyRepository {

    fun currency(): Single<ExchangeRates>

    fun selectedCurrency(): Observable<String>
}
