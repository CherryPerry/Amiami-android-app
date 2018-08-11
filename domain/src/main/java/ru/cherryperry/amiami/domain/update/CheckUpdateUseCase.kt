package ru.cherryperry.amiami.domain.update

import ru.cherryperry.amiami.domain.ObservableUseCase
import ru.cherryperry.amiami.domain.di.ApplicationVersion
import ru.cherryperry.amiami.domain.model.UpdateInfo
import ru.cherryperry.amiami.domain.model.Version
import ru.cherryperry.amiami.domain.repository.UpdateRepository
import rx.Observable
import javax.inject.Inject

class CheckUpdateUseCase @Inject constructor(
    private val updateRepository: UpdateRepository,
    @ApplicationVersion currentVersion: String
) : ObservableUseCase<Any, UpdateInfo>() {

    private val version = try {
        Version.fromString(currentVersion)
    } catch (exception: IllegalArgumentException) {
        Version(Int.MAX_VALUE)
    }

    override fun run(param: Any): Observable<UpdateInfo> = updateRepository.latestRelease()
        .filter { it.version > version }
}
