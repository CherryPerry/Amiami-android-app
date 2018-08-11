package ru.cherryperry.amiami.domain.highlight

import ru.cherryperry.amiami.domain.CompletableUseCase
import ru.cherryperry.amiami.domain.repository.HighlightRepository
import javax.inject.Inject

class HighlightListRemoveUseCase @Inject constructor(
    private val highlightRepository: HighlightRepository
) : CompletableUseCase<String>() {

    override fun run(param: String) = highlightRepository.remove(param)
}
