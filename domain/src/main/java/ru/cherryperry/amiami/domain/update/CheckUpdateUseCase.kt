package ru.cherryperry.amiami.domain.update

import io.reactivex.Flowable
import ru.cherryperry.amiami.domain.FlowableUseCase
import ru.cherryperry.amiami.domain.di.ApplicationVersion
import ru.cherryperry.amiami.domain.model.UpdateInfo
import ru.cherryperry.amiami.domain.model.Version
import ru.cherryperry.amiami.domain.repository.UpdateRepository
import javax.inject.Inject

class CheckUpdateUseCase @Inject constructor(
    private val updateRepository: UpdateRepository,
    @ApplicationVersion currentVersion: String
) : FlowableUseCase<Unit, UpdateInfo>() {

    private val version = try {
        Version.fromString(currentVersion)
    } catch (exception: IllegalArgumentException) {
        Version(Int.MAX_VALUE)
    }

    // TODO Maybe
    override fun run(param: Unit): Flowable<UpdateInfo> =
        updateRepository.latestRelease().filter { it.version > version }.toFlowable()
}
