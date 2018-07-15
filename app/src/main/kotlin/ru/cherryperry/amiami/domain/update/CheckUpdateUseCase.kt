package ru.cherryperry.amiami.domain.update

import ru.cherryperry.amiami.BuildConfig
import ru.cherryperry.amiami.data.network.github.GitHubApi
import ru.cherryperry.amiami.domain.SingleUseCase
import rx.Single
import javax.inject.Inject

class CheckUpdateUseCase @Inject constructor(
    private val gitHubApi: GitHubApi
) : SingleUseCase<Any, UpdateInfo?>() {

    override fun run(param: Any): Single<UpdateInfo?> {
        return gitHubApi.latestRelease()
            .map {
                val assetUrl = it.assets?.first()?.url
                if (it.tagName != null && it.tagName != BuildConfig.VERSION_NAME && it.name != null && assetUrl != null) {
                    UpdateInfo(it.tagName, it.name, assetUrl)
                } else {
                    null
                }
            }
            .toSingle()
    }
}