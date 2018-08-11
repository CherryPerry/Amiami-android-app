package ru.cherryperry.amiami.domain.repository

import ru.cherryperry.amiami.domain.model.UpdateInfo
import rx.Observable

interface UpdateRepository {

    /**
     * Latest release info or nothing if no releases.
     */
    fun latestRelease(): Observable<UpdateInfo>
}
