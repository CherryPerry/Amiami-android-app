package ru.cherryperry.amiami.domain.export

import io.reactivex.Completable
import ru.cherryperry.amiami.domain.CompletableUseCase
import ru.cherryperry.amiami.domain.repository.HighlightExportRepository
import ru.cherryperry.amiami.domain.repository.HighlightRepository
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
            .firstElement()
            .flatMapCompletable { exportRepository.export(it.rules, param) }
}
