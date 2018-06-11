package ru.cherryperry.amiami.data.network.github

import retrofit2.http.GET
import rx.Observable

interface GitHubApi {
    companion object {
        const val URL = "https://api.github.com/"
    }

    @GET("repos/CherryPerry/Amiami-android-app/releases/latest")
    fun latestRelease(): Observable<GitHubRelease>
}