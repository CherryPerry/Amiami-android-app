package ru.cherryperry.amiami.domain.currency

import ru.cherryperry.amiami.domain.SingleUseCase
import ru.cherryperry.amiami.domain.model.ExchangeRates
import ru.cherryperry.amiami.domain.repository.CurrencyRepository
import rx.Single
import javax.inject.Inject

class GetCurrentRatesUseCase @Inject constructor(
    private val currencyRepository: CurrencyRepository
) : SingleUseCase<Unit, ExchangeRates>() {

    override fun run(param: Unit): Single<ExchangeRates> = currencyRepository.currency()
}
