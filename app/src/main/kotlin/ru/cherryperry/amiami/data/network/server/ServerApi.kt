package ru.cherryperry.amiami.data.network.server

import retrofit2.http.GET
import rx.Single

interface ServerApi {
    companion object {
        const val URL = "https://amiami-preowned.firebaseapp.com"
    }

    @GET("v1/data")
    fun items(): Single<List<RemoteItem>>

    @GET("v1/currency")
    fun currency(): Single<ExchangeRate>
}
