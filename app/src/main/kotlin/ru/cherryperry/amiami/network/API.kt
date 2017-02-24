package ru.cherryperry.amiami.network

import retrofit2.http.GET
import rx.Observable

interface API {
    @GET("v1/data")
    fun items(): Observable<List<RemoteItem>>

    @GET("v1/currency")
    fun currency(): Observable<String>
}
