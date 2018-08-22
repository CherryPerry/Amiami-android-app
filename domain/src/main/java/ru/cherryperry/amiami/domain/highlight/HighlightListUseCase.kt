package ru.cherryperry.amiami.domain.highlight

import io.reactivex.Flowable
import ru.cherryperry.amiami.domain.FlowableUseCase
import ru.cherryperry.amiami.domain.model.HighlightRule
import ru.cherryperry.amiami.domain.repository.HighlightRepository
import javax.inject.Inject

class HighlightListUseCase @Inject constructor(
    private val highlightRepository: HighlightRepository
) : FlowableUseCase<Unit, List<HighlightRule>>() {

    override fun run(param: Unit): Flowable<List<HighlightRule>> =
        highlightRepository.configuration().map { it.rules }
}
