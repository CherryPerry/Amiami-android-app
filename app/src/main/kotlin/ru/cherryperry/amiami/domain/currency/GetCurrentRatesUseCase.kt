package ru.cherryperry.amiami.domain.currency

import ru.cherryperry.amiami.data.network.ApiProvider
import ru.cherryperry.amiami.data.network.server.ExchangeRate
import ru.cherryperry.amiami.domain.SingleUseCase
import rx.Single
import javax.inject.Inject

class GetCurrentRatesUseCase @Inject constructor(
        private val exchangeApi: ApiProvider
) : SingleUseCase<Any, ExchangeRate>() {

    override fun run(param: Any): Single<ExchangeRate> {
        return exchangeApi.api()
                .flatMap { it.currency() }
                .toSingle()
    }
}