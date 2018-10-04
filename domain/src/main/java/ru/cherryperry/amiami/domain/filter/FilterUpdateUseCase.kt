package ru.cherryperry.amiami.domain.filter

import io.reactivex.Completable
import ru.cherryperry.amiami.domain.CompletableUseCase
import ru.cherryperry.amiami.domain.repository.FilterRepository
import javax.inject.Inject

class FilterUpdateUseCase @Inject constructor(
    private val filterRepository: FilterRepository
) : CompletableUseCase<FilterUpdateParams>() {

    override fun run(param: FilterUpdateParams): Completable =
        when (param) {
            is MinFilterUpdateParams -> filterRepository.setMin(param.min)
            is MaxFilterUpdateParams -> filterRepository.setMax(param.max)
            is TermFilterUpdateParams -> filterRepository.setTerm(param.term)
            is ResetFilterUpdateParams -> filterRepository.reset()
        }
}
