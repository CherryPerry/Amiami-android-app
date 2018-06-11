package ru.cherryperry.amiami.data.network.server

import retrofit2.http.GET
import rx.Observable

interface ServerApi {

    @GET("v1/data")
    fun items(): Observable<List<RemoteItem>>

    @GET("v1/currency")
    fun currency(): Observable<ExchangeRate>
}
