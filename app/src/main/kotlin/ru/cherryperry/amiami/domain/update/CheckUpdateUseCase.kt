package ru.cherryperry.amiami.domain.update

import ru.cherryperry.amiami.BuildConfig
import ru.cherryperry.amiami.data.network.ApiProvider
import ru.cherryperry.amiami.domain.SingleUseCase
import ru.cherryperry.amiami.domain.UseCaseParam
import ru.cherryperry.amiami.domain.UseCaseResult
import rx.Single
import javax.inject.Inject

class CheckUpdateUseCase @Inject constructor(private val apiProvider: ApiProvider)
    : SingleUseCase<CheckUpdateUseCaseParams, CheckUpdateUseCaseResult>() {

    override fun run(param: CheckUpdateUseCaseParams): Single<CheckUpdateUseCaseResult> {
        return apiProvider.gitHubApi()
                .flatMap { it.latestRelease() }
                .map {
                    val assetUrl = it.assets?.first()?.url
                    if (it.tagName != null && it.tagName != BuildConfig.VERSION_NAME && it.name != null && assetUrl != null) {
                        return@map CheckUpdateUseCaseResult(UpdateInfo(it.tagName, it.name, assetUrl))
                    }
                    CheckUpdateUseCaseResult(null)
                }
                .toSingle()
    }
}

class CheckUpdateUseCaseParams : UseCaseParam()

class CheckUpdateUseCaseResult(val updateInfo: UpdateInfo?) : UseCaseResult()