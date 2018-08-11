package ru.cherryperry.amiami.domain.export

import ru.cherryperry.amiami.domain.CompletableUseCase
import ru.cherryperry.amiami.domain.repository.HighlightExportRepository
import ru.cherryperry.amiami.domain.repository.HighlightRepository
import rx.Completable
import java.io.OutputStream
import javax.inject.Inject

/**
 * Write highlight rules to [OutputStream].
 * [OutputStream] would be closed.
 */
class ExportHighlightUseCase @Inject constructor(
    private val repository: HighlightRepository,
    private val exportRepository: HighlightExportRepository
) : CompletableUseCase<OutputStream>() {

    override fun run(param: OutputStream): Completable =
        repository.configuration()
            .first()
            .flatMapCompletable { exportRepository.export(it.rules, param) }
            .toCompletable()
}
