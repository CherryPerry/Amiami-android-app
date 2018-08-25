package ru.cherryperry.amiami.domain.filter

import io.reactivex.Completable
import ru.cherryperry.amiami.domain.CompletableUseCase
import ru.cherryperry.amiami.domain.repository.FilterRepository
import javax.inject.Inject

class FilterUpdateUseCase @Inject constructor(
    private val filterRepository: FilterRepository
) : CompletableUseCase<FilterUpdateParams>() {

    override fun run(param: FilterUpdateParams): Completable =
        filterRepository.filter()
            .take(1)
            .flatMapCompletable { filter ->
                param.resolve({
                    // if current max is lower, update it first
                    if (filter.maxPrice < it) {
                        Completable.concatArray(filterRepository.setMax(it), filterRepository.setMin(it))
                    } else {
                        filterRepository.setMin(it)
                    }
                }, {
                    // if current min is higher, update it first
                    if (filter.minPrice > it) {
                        Completable.concatArray(filterRepository.setMin(it), filterRepository.setMax(it))
                    } else {
                        filterRepository.setMax(it)
                    }
                }, {
                    filterRepository.setTerm(it)
                })
            }
}
