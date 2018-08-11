package ru.cherryperry.amiami.domain.filter

import ru.cherryperry.amiami.domain.ObservableUseCase
import ru.cherryperry.amiami.domain.model.Filter
import ru.cherryperry.amiami.domain.repository.FilterRepository
import rx.Observable
import javax.inject.Inject

class FilterGetUseCase @Inject constructor(
    private val filterRepository: FilterRepository
) : ObservableUseCase<Any, Filter>() {

    override fun run(param: Any): Observable<Filter> = filterRepository.filter()
}
