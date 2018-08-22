package ru.cherryperry.amiami.domain.export

import io.reactivex.Completable
import ru.cherryperry.amiami.domain.CompletableUseCase
import ru.cherryperry.amiami.domain.repository.HighlightExportRepository
import ru.cherryperry.amiami.domain.repository.HighlightRepository
import java.io.InputStream
import javax.inject.Inject

/**
 * Read highlight rules from [InputStream] as json.
 * [InputStream] would be closed.
 */
class ImportHighlightUseCase @Inject constructor(
    private val repository: HighlightRepository,
    private val exportRepository: HighlightExportRepository
) : CompletableUseCase<InputStream>() {

    override fun run(param: InputStream): Completable =
        exportRepository.import(param)
            .flatMapCompletable { repository.replace(it) }
}
