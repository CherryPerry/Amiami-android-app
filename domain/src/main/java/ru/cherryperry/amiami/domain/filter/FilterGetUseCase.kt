package ru.cherryperry.amiami.domain.filter

import ru.cherryperry.amiami.domain.ObservableUseCase
import ru.cherryperry.amiami.domain.model.Filter
import ru.cherryperry.amiami.domain.repository.FilterRepository
import rx.Observable
import javax.inject.Inject

class FilterGetUseCase @Inject constructor(
    private val filterRepository: FilterRepository
) : ObservableUseCase<Unit, Filter>() {

    override fun run(param: Unit): Observable<Filter> = filterRepository.filter()
}
