package ru.cherryperry.amiami.data.repository

import io.reactivex.Maybe
import ru.cherryperry.amiami.data.network.github.GitHubApi
import ru.cherryperry.amiami.domain.model.UpdateInfo
import ru.cherryperry.amiami.domain.model.Version
import ru.cherryperry.amiami.domain.repository.UpdateRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UpdateRepositoryImpl @Inject constructor(
    private val gitHubApi: GitHubApi
) : UpdateRepository {

    override fun latestRelease(): Maybe<UpdateInfo> = gitHubApi.latestRelease()
        .filter { it.assets.isNotEmpty() }
        .map {
            val assetUrl = it.assets.first().url
            UpdateInfo(Version.fromString(it.tagName), it.name, assetUrl)
        }
}
