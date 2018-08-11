package ru.cherryperry.amiami.domain.filter

import ru.cherryperry.amiami.domain.CompletableUseCase
import ru.cherryperry.amiami.domain.repository.FilterRepository
import rx.Completable
import javax.inject.Inject

class FilterUpdateUseCase @Inject constructor(
    private val filterRepository: FilterRepository
) : CompletableUseCase<FilterUpdateParams>() {

    override fun run(param: FilterUpdateParams): Completable {
        return filterRepository.filter()
            .take(1)
            .flatMapCompletable { filter ->
                param.resolve({
                    // if current max is lower, update it first
                    if (filter.maxPrice < it) {
                        Completable.concat(filterRepository.setMax(it), filterRepository.setMin(it))
                    } else {
                        filterRepository.setMin(it)
                    }
                }, {
                    // if current min is higher, update it first
                    if (filter.minPrice > it) {
                        Completable.concat(filterRepository.setMin(it), filterRepository.setMax(it))
                    } else {
                        filterRepository.setMax(it)
                    }
                }, {
                    filterRepository.setTerm(it)
                })
            }
            .toCompletable()
    }
}
