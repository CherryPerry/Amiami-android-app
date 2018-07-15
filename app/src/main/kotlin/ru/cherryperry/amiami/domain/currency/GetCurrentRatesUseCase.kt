package ru.cherryperry.amiami.domain.currency

import ru.cherryperry.amiami.data.network.server.ExchangeRate
import ru.cherryperry.amiami.data.network.server.ServerApi
import ru.cherryperry.amiami.domain.SingleUseCase
import rx.Single
import javax.inject.Inject

class GetCurrentRatesUseCase @Inject constructor(
    private val serverApi: ServerApi
) : SingleUseCase<Any, ExchangeRate>() {

    override fun run(param: Any): Single<ExchangeRate> {
        return serverApi.currency()
    }
}