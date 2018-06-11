package ru.cherryperry.amiami.domain

import rx.Observable

/**
 * Some logic, that produces result for presenter with provided params
 */
abstract class ObservableUseCase<in Params, Result> {

    abstract fun run(param: Params): Observable<Result>
}