package ru.cherryperry.amiami.model

import ru.cherryperry.amiami.data.network.server.ExchangeRate
import ru.cherryperry.amiami.domain.currency.GetCurrentRatesUseCase
import rx.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrencyRepository @Inject constructor(
        private val getCurrentRatesUseCase: GetCurrentRatesUseCase
) {

    fun exchangeRate(forceNetwork: Boolean): Observable<ExchangeRate> {
        return getCurrentRatesUseCase.run(Any())
                .toObservable()
    }
}