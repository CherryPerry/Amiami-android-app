package ru.cherryperry.amiami.domain.repository

import io.reactivex.Maybe
import ru.cherryperry.amiami.domain.model.UpdateInfo

interface UpdateRepository {

    /**
     * Latest release info or nothing if no releases.
     */
    fun latestRelease(): Maybe<UpdateInfo>
}
