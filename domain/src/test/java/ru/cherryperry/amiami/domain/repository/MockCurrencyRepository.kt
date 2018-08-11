package ru.cherryperry.amiami.domain.repository

import ru.cherryperry.amiami.domain.model.ExchangeRates
import rx.Observable
import rx.Single

open class MockCurrencyRepository : CurrencyRepository {

    override fun currency(): Single<ExchangeRates> {
        throw NotImplementedError()
    }

    override fun selectedCurrency(): Observable<String> {
        throw NotImplementedError()
    }
}
