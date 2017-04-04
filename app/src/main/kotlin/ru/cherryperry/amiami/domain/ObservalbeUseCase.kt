package ru.cherryperry.amiami.domain

import rx.Observable

/**
 * Some logic, that produces result for presenter with provided params
 */
abstract class ObservalbeUseCase<in Params : UseCaseParam, Result : UseCaseResult> {

    abstract fun run(param: Params): Observable<Result>
}