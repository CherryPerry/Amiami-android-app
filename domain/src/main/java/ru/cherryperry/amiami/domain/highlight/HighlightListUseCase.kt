package ru.cherryperry.amiami.domain.highlight

import ru.cherryperry.amiami.domain.ObservableUseCase
import ru.cherryperry.amiami.domain.model.HighlightRule
import ru.cherryperry.amiami.domain.repository.HighlightRepository
import rx.Observable
import javax.inject.Inject

class HighlightListUseCase @Inject constructor(
    private val highlightRepository: HighlightRepository
) : ObservableUseCase<Any, List<HighlightRule>>() {

    override fun run(param: Any): Observable<List<HighlightRule>> =
        highlightRepository.configuration().map { it.rules }
}
