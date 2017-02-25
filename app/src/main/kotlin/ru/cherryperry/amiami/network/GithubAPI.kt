package ru.cherryperry.amiami.network

import retrofit2.http.GET
import rx.Observable

interface GitHubAPI {
    companion object {
        const val URL = "https://api.github.com/"
    }

    @GET("repos/CherryPerry/Amiami-android-app/releases/latest")
    fun latestRelease(): Observable<GitHubRelease>
}