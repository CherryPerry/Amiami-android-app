package ru.cherryperry.amiami.domain.highlight

import ru.cherryperry.amiami.domain.CompletableUseCase
import ru.cherryperry.amiami.domain.model.HighlightRule
import ru.cherryperry.amiami.domain.repository.HighlightRepository
import javax.inject.Inject

class HighlightListAddUseCase @Inject constructor(
    private val highlightRepository: HighlightRepository
) : CompletableUseCase<HighlightRule>() {

    override fun run(param: HighlightRule) = highlightRepository.add(param)
}
