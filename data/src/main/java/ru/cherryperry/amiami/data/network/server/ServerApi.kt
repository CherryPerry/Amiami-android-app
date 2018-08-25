package ru.cherryperry.amiami.data.network.server

import io.reactivex.Single
import retrofit2.http.GET
import ru.cherryperry.amiami.domain.model.ExchangeRates

interface ServerApi {

    companion object {
        const val URL = "https://amiami-preowned.firebaseapp.com"
    }

    @GET("v1/data")
    fun items(): Single<List<RemoteItem>>

    @GET("v1/currency")
    fun currency(): Single<ExchangeRates>
}
