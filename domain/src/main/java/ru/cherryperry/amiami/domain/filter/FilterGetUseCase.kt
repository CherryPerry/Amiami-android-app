package ru.cherryperry.amiami.domain.filter

import ru.cherryperry.amiami.domain.FlowableUseCase
import ru.cherryperry.amiami.domain.model.Filter
import ru.cherryperry.amiami.domain.repository.FilterRepository
import javax.inject.Inject

class FilterGetUseCase @Inject constructor(
    private val filterRepository: FilterRepository
) : FlowableUseCase<Unit, Filter>() {

    override fun run(param: Unit) = filterRepository.filter()
}
