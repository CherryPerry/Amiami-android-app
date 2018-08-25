package ru.cherryperry.amiami.data.network.github

import io.reactivex.Single
import retrofit2.http.GET

interface GitHubApi {

    companion object {
        const val URL = "https://api.github.com/"
    }

    @GET("repos/CherryPerry/Amiami-android-app/releases/latest")
    fun latestRelease(): Single<GitHubRelease>
}
