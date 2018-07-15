package ru.cherryperry.amiami.data.network

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import ru.cherryperry.amiami.data.network.github.GitHubApi
import ru.cherryperry.amiami.data.network.server.ServerApi
import javax.inject.Inject
import javax.inject.Singleton

@Module
class ApiModule @Inject constructor() {

    @Singleton
    @Provides
    fun serverApi(retrofitBuilder: Retrofit.Builder): ServerApi = retrofitBuilder
        .baseUrl(ServerApi.URL)
        .build()
        .create(ServerApi::class.java)

    @Singleton
    @Provides
    fun gitHubApi(retrofitBuilder: Retrofit.Builder): GitHubApi = retrofitBuilder
        .baseUrl(GitHubApi.URL)
        .build()
        .create(GitHubApi::class.java)
}